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
path_data_by_year_trend = re.compile(r'\/year\/(\d{4})\/trend')
path_data_by_year_monthly_avg = re.compile(r'\/year\/(\d{4})\/avg')
path_data_by_year = re.compile(r'\/year\/(\d{4})')

def request_handler(path: str, req_payload: dict):

    dbconnect = None

    try:

        # initialize connection
        dbconnect = MySQLdb.connect(host = '127.0.0.1', 
                user = 'root', 
                passwd = '1234', 
                db = 'weight_tracking',
                sql_mode = None)

        if(match_result := path_data_by_year_month.fullmatch(path)):
            data = get_data_by_year_month(dbconnect, match_result)
            return data
    
        if(match_result := path_data_by_year_trend.fullmatch(path)):
            data = get_data_by_year_trend(dbconnect, match_result)
            return data

        if(match_result := path_data_by_year_monthly_avg.fullmatch(path)):
            data = get_data_monthly_avg_by_year(dbconnect, match_result)
            return data

        if(match_result := path_data_by_year.fullmatch(path)):
            data = get_data_by_year(dbconnect, match_result)
            return data

    finally:

        if dbconnect:
            dbconnect.close()

    logger.warning('No paths matched.')
    return {}


def get_data_by_year_trend(connection, match_result):
    '''
    Collect the min or max value for the year
    '''
    year = match_result.group(1)

    cursor = connection.cursor()

    # reset sql_mode to allow columns outside aggregate
    cursor.execute('set sql_mode = \'\'')

    # query together
    cursor.execute(f'''
        with year_data as (
            select *
            from weight_entries
            where year(entry_date) = 2022
        )
        select min(entry_value), max(entry_value), avg(entry_value)
        from year_data
    ''')

    aggregates = {}
    for _min, _max, _avg in cursor.fetchall():
        aggregates = {
            'min': _min,
            'max': _max,
            'avg': _avg
        }

    return {
        'year': year,
        'data': aggregates
    }


def get_data_monthly_avg_by_year(connection, match_result):
    '''
    Collect monthly averages by year

    e.g., /year/2022?avg
    '''
    year = match_result.group(1)

    logger.debug(f'Collecting monthly average for year {year}.')

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


def get_data_by_year_month(connection, match_result):
    '''
    Collect data for a single month and year.

    e.g., /year/2022/month/02
    '''
    year = match_result.group(1)
    month = match_result.group(2)

    logger.debug(f'Collecting data for year {year}.')

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


def get_data_by_year(connection, match_result):
    '''
    Collect data for a given year

    e.g., /year/2021
    '''
    year = match_result.group(1)

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

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions by request routes.')
    parser.add_argument('path', default = '/', type = str, help = 'Path to execute. e.g., /year/2023')
    parser.add_argument('--indent', default = None, type = int, help = 'Indentation size on json pretty print.  Default None to be less verbose during dev/test.')
    args = parser.parse_args()

    # verify path and response
    response = request_handler(args.path, {})
    json.dump(response, sys.stdout
              , indent = args.indent
              , default = str)