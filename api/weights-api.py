import os, re, json, sys
import logging, argparse

import boto3

dynamodb = boto3.resource('dynamodb')

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

path_data_available_years = re.compile(r'\/years')
path_data_by_year_month = re.compile(r'\/year\/(\d{4})\/month\/(\d{2})')
path_data_by_year_trend = re.compile(r'\/year\/(\d{4})\/trend')
path_data_by_year_monthly_avg = re.compile(r'\/year\/(\d{4})\/avg')
path_data_by_year = re.compile(r'\/year\/(\d{4})')

def request_handler(path: str, req_payload: dict):
    pass


def put_entry(payload):
    table = dynamodb.Table('Weights')
    table.put_item(
        Item = {
            'pk': payload['guid'],
            'sk': payload['entry-date'],
            'value': payload['value']
        }
    )

    pass


def get_available_years(connection):
    '''
    Collect availible years to select from
    '''
    pass


def get_data_by_year_trend(connection, match_result):
    '''
    Collect the min or max value for the year
    '''
    pass


def get_data_monthly_avg_by_year(connection, match_result):
    '''
    Collect monthly averages by year

    e.g., /year/2022?avg
    '''
    pass


def get_data_by_year_month(connection, match_result):
    '''
    Collect data for a single month and year.

    e.g., /year/2022/month/02
    '''
    pass


def get_data_by_year(connection, match_result):
    '''
    Collect data for a given year

    e.g., /year/2021
    '''
    pass

if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions by request routes.')
    parser.add_argument('path', default = '/', type = str, help = 'Path to execute. e.g., /year/2023')
    parser.add_argument('--indent', default = None, type = int, help = 'Indentation size on json pretty print.  Default None to be less verbose during dev/test.')
    args = parser.parse_args()

    # verify path and response
    response = request_handler(args.path, {})
    json.dump(response, sys.stdout
              , indent = args.indent
              , default = str)