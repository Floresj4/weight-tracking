import os, re, json, sys
import logging, argparse

import MySQLdb

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


def request_handler(path: str, req_payload: dict):

    dbconnect = None

    try:

        # initialize connection
        dbconnect = MySQLdb.connect(host = '127.0.0.1', 
                user = 'root', 
                passwd = '1234', 
                db = 'weight_tracking')

        if(path == '/'):
            return {}

        # not sure this is the endpoint I want yet
        elif(path == '/year'):

            year = 2023
            data = get_data_by_year(dbconnect, year)

            return data

    finally:

        if dbconnect:
            dbconnect.close()

    return {}


def get_data_by_year(connection, year: int):
    '''
    Collect data for a given year
    '''
    logger.debug(f'Collecting data for year {year}')
    
    cursor = connection.cursor()
    cursor.execute(f'''
        select * 
        from weight_entries 
        where year(entry_date) = {year}
        ''')

    yearly_data = []
    for _date, _value in cursor.fetchall():
        yearly_data.append({
            'entry_date': _date,
            'entry_value': _value
            })

    return {
        'year': year,
        'data': yearly_data
    }


if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions')
    parser.add_argument('path', default = '/', type = str)
    args = parser.parse_args()

    response = request_handler(args.path, {})
    json.dump(response, sys.stdout, indent = 4, default = str)