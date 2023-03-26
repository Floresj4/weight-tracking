import os
import logging, argparse

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

    if(path == '/'):
        
        return {}
    
    # not sure this is the endpoint I want yet
    elif(path == '/year'):

        year = 2023
        data = get_data_by_year(year)

        return data


    return {}


def get_data_by_year(year: int):
    logger.debug(f'Collecting data for year {year}')
    
    return {}


if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions')
    parser.add_argument('path', default = '/', type = str)
    args = parser.parse_args()

    response = request_handler(args.path, {})
    print(response)