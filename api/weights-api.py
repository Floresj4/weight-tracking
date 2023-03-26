import argparse

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
    pass

if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions')
    parser.add_argument('path', default = '/', type = str)
    args = parser.parse_args()

    response = request_handler(args.path, {})
    print(response)