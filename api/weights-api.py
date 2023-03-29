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

path_data_by_year_month = re.compile(r'\/year\/(\d{4})\/month\/(\d{2})')
path_data_by_year_monthly_avg = re.compile(r'\/year\/(\d{4})\?avg')
path_data_by_year = re.compile(r'\/year\/(\d{4})')

def request_handler(path: str, req_payload: dict):

    dbconnect = None

    try:

        # initialize connection
        dbconnect = MySQLdb.connect(host = '127.0.0.1', 
                user = 'root', 
                passwd = '1234', 
                db = 'weight_tracking')

        if(match_result := path_data_by_year_month.fullmatch(path)):
            logger.debug(match_result)

            year = match_result.group(1)
            month = match_result.group(2)
            data = get_data_by_year_month(dbconnect, year, month)
            return data

        if(match_result := path_data_by_year_monthly_avg.fullmatch(path)):

            year = match_result.group(1)
            data = get_monthly_avg_by_year(dbconnect, year)
            return data

        if(match_result := path_data_by_year.fullmatch(path)):
            logger.debug(match_result)

            year = match_result.group(1)
            data = get_data_by_year(dbconnect, year)
            return data

    finally:

        if dbconnect:
            dbconnect.close()

    logger.warning('No paths matched.')
    return {}


def get_monthly_avg_by_year(connection, year: int):
    '''
    Collect monthly averages by year
    '''
    logger.debug(f'Collecting average for year {year}')

    cursor = connection.cursor()
    cursor.execute(f'''
        select 
            month(entry_date)
            , truncate(avg(entry_value),2) 
        from weight_entries 
        where year(entry_date) = {year} 
        group by month(entry_date);
    ''')

    month_data = []
    for _date, _value in cursor.fetchall():
        month_data.append({
            'entry_date': _date,
            'entry_value': _value
            })

    return {
        'year': year,
        'data': month_data
    }


def get_data_by_year_month(connection, year: int, month: int):
    '''
    Collect data for a single month and year
    '''
    logger.debug(f'Collecting data for year {year}, {month}')

    cursor = connection.cursor()
    cursor.execute(f'''
        select *
        from weight_entries
        where year(entry_date) = {year}
            and month(entry_date) = {month}
    ''')

    month_data = []
    for _date, _value in cursor.fetchall():
        month_data.append({
            'entry_date': _date,
            'entry_value': _value
            })

    return {
        'year': year,
        'month': month,
        'data': month_data
    }


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
    parser.add_argument('--indent', default = None, type = int)
    args = parser.parse_args()

    # test path and response
    response = request_handler(args.path, {})
    json.dump(response, sys.stdout
              , indent = args.indent
              , default = str)