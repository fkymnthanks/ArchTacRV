import os

# 训练数据相对路径
PING_ECHO_NAME_DATA_FILE = r"\traindata\pingecho\namedata.csv"
HEARTBEAT_NAME_DATA_FILE = r"\traindata\heartbeat\namedata.csv"
VOTING_NAME_DATA_FILE = r"\traindata\voting\namedata.csv"
REDUNDANCY_NAME_DATA_FILE = r"\traindata\redundancy\namedata.csv"
CHECKPOINT_NAME_DATA_FILE = r"\traindata\checkpoint\namedata.csv"
FIFO_NAME_DATA_FILE = r"\traindata\fifo\namedata.csv"
PRIORITY_NAME_DATA_FILE = r"\traindata\priority\namedata.csv"
CACHE_NAME_DATA_FILE = r"\traindata\cache\namedata.csv"
AUTHENTICATE_NAME_DATA_FILE = r"\traindata\authenticate\namedata.csv"
AUTHORIZE_NAME_DATA_FILE = r"\traindata\authorize\namedata.csv"
# 测试数据相对路径
PING_ECHO_NAME_DATA_TEST_FILE = r"\testdata\pingecho\testnamedata.csv"
HEARTBEAT_NAME_DATA_TEST_FILE = r"\testdata\heartbeat\testnamedata.csv"
VOTING_NAME_DATA_TEST_FILE = r"\testdata\voting\testnamedata.csv"
REDUNDANCY_NAME_DATA_TEST_FILE = r"\testdata\redundancy\testnamedata.csv"
CHECKPOINT_NAME_DATA_TEST_FILE = r"\testdata\checkpoint\testnamedata.csv"
FIFO_NAME_DATA_TEST_FILE = r"\testdata\fifo\testnamedata.csv"
PRIORITY_NAME_DATA_TEST_FILE = r"\testdata\priority\testnamedata.csv"
CACHE_NAME_DATA_TEST_FILE = r"\testdata\cache\testnamedata.csv"
AUTHENTICATE_NAME_DATA_TEST_FILE = r"\testdata\authenticate\testnamedata.csv"
AUTHORIZE_NAME_DATA_TEST_FILE = r"\testdata\authorize\testnamedata.csv"


def get_project_path():
    # os.path.realpath获取当前脚本的绝对路径
    cur_path = os.path.dirname(os.path.realpath(__file__))
    # 获取当前根项目的路径
    project_path = os.path.dirname(os.path.dirname(cur_path))
    return project_path


def get_ping_echo_name_data():
    return get_project_path() + PING_ECHO_NAME_DATA_FILE


def get_ping_echo_name_data_test():
    return get_project_path() + PING_ECHO_NAME_DATA_TEST_FILE


def get_heartbeat_name_data():
    return get_project_path() + HEARTBEAT_NAME_DATA_FILE


def get_heartbeat_name_data_test():
    return get_project_path() + HEARTBEAT_NAME_DATA_TEST_FILE


def get_voting_name_data():
    return get_project_path() + VOTING_NAME_DATA_FILE


def get_voting_name_data_test():
    return get_project_path() + VOTING_NAME_DATA_TEST_FILE


def get_redundancy_name_data():
    return get_project_path() + REDUNDANCY_NAME_DATA_FILE


def get_redundancy_name_data_test():
    return get_project_path() + REDUNDANCY_NAME_DATA_TEST_FILE


def get_checkpoint_name_data():
    return get_project_path() + CHECKPOINT_NAME_DATA_FILE


def get_checkpoint_name_data_test():
    return get_project_path() + CHECKPOINT_NAME_DATA_TEST_FILE


def get_fifo_name_data():
    return get_project_path() + FIFO_NAME_DATA_FILE


def get_fifo_name_data_test():
    return get_project_path() + FIFO_NAME_DATA_TEST_FILE


def get_priority_name_data():
    return get_project_path() + PRIORITY_NAME_DATA_FILE


def get_priority_name_data_test():
    return get_project_path() + PRIORITY_NAME_DATA_TEST_FILE


def get_cache_name_data():
    return get_project_path() + CACHE_NAME_DATA_FILE


def get_cache_name_data_test():
    return get_project_path() + CACHE_NAME_DATA_TEST_FILE


def get_authenticate_name_data():
    return get_project_path() + AUTHENTICATE_NAME_DATA_FILE


def get_authenticate_name_data_test():
    return get_project_path() + AUTHENTICATE_NAME_DATA_TEST_FILE


def get_authorize_name_data():
    return get_project_path() + AUTHORIZE_NAME_DATA_FILE


def get_authorize_name_data_test():
    return get_project_path() + AUTHORIZE_NAME_DATA_TEST_FILE
