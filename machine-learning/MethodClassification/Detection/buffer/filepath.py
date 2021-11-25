import os

# 训练数据相对路径
PING_ECHO_TRAIN_FILE = r"\traindata\pingecho\train.csv"
HEARTBEAT_TRAIN_FILE = r"\traindata\heartbeat\train.csv"
VOTING_TRAIN_FILE = r"\traindata\voting\train.csv"
REDUNDANCY_TRAIN_FILE = r"\traindata\redundancy\train.csv"
CHECKPOINT_TRAIN_FILE = r"\traindata\checkpoint\train.csv"
FIFO_TRAIN_FILE = r"\traindata\fifo\train.csv"
PRIORITY_TRAIN_FILE = r"\traindata\priority\train.csv"
CACHE_TRAIN_FILE = r"\traindata\cache\train.csv"
AUTHENTICATE_TRAIN_FILE = r"\traindata\authenticate\train.csv"
AUTHORIZE_TRAIN_FILE = r"\traindata\authorize\train.csv"
# 测试数据相对路径
PING_ECHO_TEST_FILE = r"\testdata\pingecho\test.csv"
HEARTBEAT_TEST_FILE = r"\testdata\heartbeat\test.csv"
VOTING_TEST_FILE = r"\testdata\voting\test.csv"
REDUNDANCY_TEST_FILE = r"\testdata\redundancy\test.csv"
CHECKPOINT_TEST_FILE = r"\testdata\checkpoint\test.csv"
FIFO_TEST_FILE = r"\testdata\fifo\test.csv"
PRIORITY_TEST_FILE = r"\testdata\priority\test.csv"
CACHE_TEST_FILE = r"\testdata\cache\test.csv"
AUTHENTICATE_TEST_FILE = r"\testdata\authenticate\test.csv"
AUTHORIZE_TEST_FILE = r"\testdata\authorize\test.csv"

fileDict = {"ping_echo_train": r"\traindata\pingecho\train.csv",
            "heartbeat_train": r"\traindata\heartbeat\train.csv",
            "voting_train": r"\traindata\voting\train.csv",
            "redundancy_train": r"\traindata\redundancy\train.csv",
            "checkpoint_train": r"\traindata\checkpoint\train.csv",
            "fifo_train": r"\traindata\fifo\train.csv",
            "priority_train": r"\traindata\priority\train.csv",
            "cache_train": r"\traindata\cache\train.csv",
            "authenticate_train": r"\traindata\authenticate\train.csv",
            "authorize_train": r"\traindata\authorize\train.csv",
            "ping_echo_test": r"\testdata\pingecho\test.csv",
            "heartbeat_test": r"\testdata\heartbeat\test.csv",
            "voting_test": r"\traindata\voting\train.csv",
            "redundancy_test": r"\testdata\redundancy\test.csv",
            "checkpoint_test": r"\testdata\checkpoint\test.csv",
            "fifo_test": r"\testdata\fifo\test.csv",
            "priority_test": r"\testdata\priority\test.csv",
            "cache_test": r"\testdata\cache\test.csv",
            "authenticate_test": r"\testdata\authenticate\test.csv",
            "authorize_test": r"\testdata\authorize\test.csv"}


def get_project_path():
    # os.path.realpath获取当前脚本的绝对路径
    cur_path = os.path.dirname(os.path.realpath(__file__))
    # 获取当前根项目的路径
    project_path = os.path.dirname(os.path.dirname(cur_path))
    return project_path


def get_tactic_data(tactic_key):
    return get_project_path() + fileDict[tactic_key]


def get_ping_echo_train():
    return get_project_path() + PING_ECHO_TRAIN_FILE


def get_ping_echo_test():
    return get_project_path() + PING_ECHO_TEST_FILE


def get_heartbeat_train():
    return get_project_path() + HEARTBEAT_TRAIN_FILE


def get_heartbeat_test():
    return get_project_path() + HEARTBEAT_TEST_FILE


def get_voting_train():
    return get_project_path() + VOTING_TRAIN_FILE


def get_voting_test():
    return get_project_path() + VOTING_TEST_FILE


def get_redundancy_train():
    return get_project_path() + REDUNDANCY_TRAIN_FILE


def get_redundancy_test():
    return get_project_path() + REDUNDANCY_TEST_FILE


def get_checkpoint_train():
    return get_project_path() + CHECKPOINT_TRAIN_FILE


def get_checkpoint_test():
    return get_project_path() + CHECKPOINT_TEST_FILE


def get_fifo_train():
    return get_project_path() + FIFO_TRAIN_FILE


def get_fifo_test():
    return get_project_path() + FIFO_TEST_FILE


def get_priority_train():
    return get_project_path() + PRIORITY_TRAIN_FILE


def get_priority_test():
    return get_project_path() + PRIORITY_TEST_FILE


def get_cache_train():
    return get_project_path() + CACHE_TRAIN_FILE


def get_cache_test():
    return get_project_path() + CACHE_TEST_FILE


def get_authenticate_train():
    return get_project_path() + AUTHENTICATE_TRAIN_FILE


def get_authenticate_test():
    return get_project_path() + AUTHENTICATE_TEST_FILE


def get_authorize_train():
    return get_project_path() + AUTHORIZE_TRAIN_FILE


def get_authorize_test():
    return get_project_path() + AUTHORIZE_TEST_FILE
