import os, re, json, sys
import urllib.parse
import logging, argparse
import base64

from botocore.config import Config

import boto3
from boto3.dynamodb.conditions import Key, Attr


config = Config(
   connect_timeout = 3.0,
   read_timeout = 3.0
)

session = boto3.Session()
dynamodb = session.resource('dynamodb',
                          config = config,
                          endpoint_url = "http://localhost:8000/")

def initialize_logger(name: str = __name__):
    '''
    initialize logger
    '''

    FORMAT = '[%(levelname)s]:%(asctime)s %(message)s'
    logging.basicConfig(format = FORMAT)
    logger = logging.getLogger(name)
    logger.setLevel(os.getenv('LOGGING_LEVEL', 'DEBUG'))
    return logger


logger = initialize_logger()

path_data_new = re.compile(r'\/new')
path_data_available_years = re.compile(r'\/years')
path_data_by_year_month = re.compile(r'\/year\/(\d{4})\/month\/(\d{1,2})')
path_data_by_year_trend = re.compile(r'\/year\/(\d{4})\/trend')
path_data_by_year_monthly_avg = re.compile(r'\/year\/(\d{4})\/avg')
path_data_by_year = re.compile(r'\/year\/(\d{4})')

def request_handler(event: str, context: dict):

    path = event['rawPath']
    query_params = event['queryStringParameters']
    post_body = event['body']

    response = {}
    if(match_result := path_data_new.fullmatch(path)):
        response = put_entry(post_body)
    
    elif(match_result := path_data_by_year.fullmatch(path)):
        response = get_data_by_year(path, query_params)
    
    elif(match_result := path_data_by_year_month.fullmatch(path)):
        response = get_data_by_year_month(path, query_params)
    
    elif(match_result := path_data_available_years.fullmatch(path)):
        response = get_available_years(path, query_params)
    
    return response


def put_entry(encoded_body):
    '''
    Put a single entry into the table
    '''
    decoded_body = base64.b64decode(encoded_body).decode('utf-8')
    decoded_body = json.loads(decoded_body)
    logger.info(f'put_entry: {decoded_body}')

    table = dynamodb.Table('Weights')
    table.put_item(
        Item = {
            'guid': decoded_body['guid'],
            'entry-date': decoded_body['entry-date'],
            'value': decoded_body['value']
        }
    )

    return 'Ok'


def get_available_years(path: str, query_params: dict):
    '''
    Collect availible years to select from
    '''
    logger.info(f'get_available_years: {path}, {query_params}')

    guid = query_params['guid']

    table = dynamodb.Table('WeightUsers')    
    response = table.query(
        KeyConditionExpression = Key('guid').eq(guid),
        ProjectionExpression = 'entry-years'
    )

    # return if exists
    if 'Items' in response:
        return response['Items']

    return { 'Items': [] }


def get_data_by_year_trend(match_result):
    '''
    Collect the min or max value for the year
    '''
    pass


def get_data_monthly_avg_by_year(match_result):
    '''
    Collect monthly averages by year

    e.g., /year/2022?avg
    '''
    pass


def get_data_by_year_month(path, query_params):
    '''
    Collect data for a single month and year.
    e.g., /year/2022/month/02
    '''
    logger.info(f'get_data_by_year: {path}, {query_params}')

    path_parts = path.split('/')
    guid = query_params['guid']
    year = path_parts[2]
    month = path_parts[4].zfill(2)
    year_month = f'{year}-{month}'

    table = dynamodb.Table('Weights')
    response = table.query(
        KeyConditionExpression = Key('guid').eq(guid) \
            & Key('entry-date').begins_with(year_month)
    )

    # return if exists
    if 'Items' in response:
        return response['Items']

    return { 'Items': [] }


def get_data_by_year(path, query_params):
    '''
    Collect data for a given year
    e.g., /year/2021
    '''
    logger.info(f'get_data_by_year: {path}, {query_params}')

    guid = query_params['guid']
    year = path.split('/')[2]

    table = dynamodb.Table('Weights')
    response = table.query(
        KeyConditionExpression = Key('guid').eq(guid) \
            & Key('entry-date').begins_with(year)
    )

    # return if exists
    if 'Items' in response:
        return response['Items']

    return { 'Items': [] }


def get_payload_from_input(input):
    '''
    Parse the argparge --payload data and create a dictionary
    structure that simulates what would come through the request handler
    '''
    split = [s.strip() for s in input.split(',')]
    if len(split) != 3:
        raise Exception(f'Payload input {input} must contain 3 values in '
            + 'the form USERGUID,DATE,VALUE')
    
    # stringify the post body
    post_body = json.dumps({
        'guid': split[0],
        'entry-date': split[1],
        'value': split[2] 
    })

    encoded_bytes = post_body.encode('utf-8')
    return base64.b64encode(encoded_bytes) \
        .decode('utf-8')


def get_event_from_inputs(args):
    '''
    Load the json event template and substitute inputs
    into the object for local testing and development
    '''
    event = {}  # load the test payload to replace values on
    with open('./test/api-gateway-http-api.json', 'r') as api_sample:
        event = json.load(api_sample)

    # parse the input url for setup
    parsed_url = urllib.parse.urlparse(args.url)
    query_params = urllib.parse.parse_qs(parsed_url.query)

    # parse_qs returns a dict{ key: value[] }
    single_value_query_params = {k: v[0] for k, v in query_params.items() if len(v) == 1}

    # parse input into a request body
    post_body = get_payload_from_input(args.body) \
        if args.body else {}
    
    # update event properties
    event['rawPath'] = parsed_url.path
    event['rawQueryString'] = parsed_url.query
    event['queryStringParameters'] = single_value_query_params
    event['body'] = post_body
    return event


if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions by request routes.')
    parser.add_argument('url', default = '/', type = str, help = 'Url endpoint to execute. e.g., /year/2023')
    parser.add_argument('--body', type = str, help = 'Comma-separate values of post data - USERGUID,DATE,VALUE e.g., 9842-1232-2321-0923,2025-01-01,175.0')
    parser.add_argument('--indent', default = None, type = int, help = 'Indentation size on json pretty print.  Default None to be less verbose during dev/test.')
    args = parser.parse_args()

    event = get_event_from_inputs(args)

    response = request_handler(event, {})
    json.dump(response, sys.stdout
              , indent = args.indent
              , default = str)