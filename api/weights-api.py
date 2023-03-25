import argparse

if __name__ == '__main__':

    parser = argparse.ArgumentParser(prog = 'Weight Tracking Access', description='Database interactions')
    parser.add_argument('path', default = '/', type = str)
    args = parser.parse_args()