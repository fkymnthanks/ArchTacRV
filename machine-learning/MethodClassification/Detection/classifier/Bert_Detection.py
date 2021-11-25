import numpy as np
from seqeval.metrics import recall_score, precision_score, f1_score

from Detection.model.Bert import model_init, model_eval, model_predict, model_train
from Detection.buffer import filepath, data_process
from sklearn.preprocessing import LabelEncoder
from sklearn import model_selection
from imblearn.over_sampling import SMOTE
from Detection.tokenizer.camel_token import camel_token
from collections import defaultdict, Counter
from sklearn.metrics import accuracy_score, confusion_matrix
from sklearn.feature_extraction.text import TfidfVectorizer
import joblib

# 确保每次生成的随即数都相同
np.random.seed(500)

train_file_path = filepath.get_ping_echo_train()
test_file_path = filepath.get_ping_echo_test()
CorpusTrain = data_process.read_csv(train_file_path)
CorpusTest = data_process.read_csv(test_file_path)
# 数据处理
# 去除含有空缺值的行
CorpusTrain['text'].dropna(inplace=True)
CorpusTest['text'].dropna(inplace=True)
# 驼峰命名分割
CorpusTrain['text'] = [camel_token(entry) for entry in CorpusTrain['text']]
CorpusTest['text'] = [camel_token(entry) for entry in CorpusTest['text']]
# 去除大小写
CorpusTrain['text'] = [str(entry).lower() for entry in CorpusTrain['text']]
CorpusTest['text'] = [str(entry).lower() for entry in CorpusTest['text']]

CorpusTrain.rename(columns={'label': 'labels'}, inplace=True)
CorpusTest.rename(columns={'label': 'labels'}, inplace=True)
Train_X = CorpusTrain['text']
Train_Y = CorpusTrain['labels']
Test_X = CorpusTest['text']
Test_Y = CorpusTest['labels']
Encoder = LabelEncoder()
Train_Y = Encoder.fit_transform(Train_Y)
Test_Y = Encoder.fit_transform(Test_Y)
label_num = len(Counter(Train_Y).keys())
print(label_num)

# 将分词后的词从CorpusTest['text']这个形式转换一个 list(str)格式
text_list = []
for entry in Test_X:
    text_list.append((str)(entry))
# 将分词后的词从CorpusTest['label']这个形式转换一个 list(int)格式
label_list = []
for entry in Test_Y:
    label_list.append((int)(entry))
if __name__ == "__main__":
    # 模型初始化
    Bert = model_init(labelnum=label_num)
    Bert.train_model(CorpusTrain, args={'fp16': False})
    result, model_outputs, wrong_predictions = Bert.eval_model(CorpusTest, acc=accuracy_score)
    # 模型精度指标
    print("result :", result)
    # 没太看懂 可能是特征向量？
    print("model_outputs :", model_outputs)
    # 目前输出是空值 不太确定
    print("wrong_predictions :", wrong_predictions)
    predictions, raw_outputs = Bert.predict(text_list)
    print("predictions :", predictions)
    print("raw_outputs :", raw_outputs)
    con_matrix = confusion_matrix(Test_Y, predictions)
    print(con_matrix)  # 输出混淆矩阵
    print("准确率：%s" % accuracy_score(Test_Y, predictions))  # 输出准确率
    recall_every_class = recall_score(Test_Y, predictions, average=None)
    recall_macro = recall_score(Test_Y, predictions, average='macro')
    recall_micro = recall_score(Test_Y, predictions, average='micro')
    recall_weighted = recall_score(Test_Y, predictions, average='weighted')
    print("召回率：%s %s %s %s" % (recall_every_class, recall_macro, recall_micro, recall_weighted))  # 输出召回率
    precision_every_class = precision_score(Test_Y, predictions, average=None)
    precision_macro = precision_score(Test_Y, predictions, average='macro')
    precision_micro = precision_score(Test_Y, predictions, average='micro')
    precision_weighted = precision_score(Test_Y, predictions, average='weighted')
    print("精确率：%s %s %s %s" % (precision_every_class, precision_macro, precision_micro, precision_weighted))  # 输出精确率
    f1_every_class = f1_score(Test_Y, predictions, average=None)
    f1_macro = f1_score(Test_Y, predictions, average='macro')
    f1_micro = f1_score(Test_Y, predictions, average='micro')
    f1_weighted = f1_score(Test_Y, predictions, average='weighted')
    print("F1值：%s %s %s %s" % (f1_every_class, f1_macro, f1_micro, f1_weighted))  # 输出F1值
# count = 0  # 统计循环次数
# accuracy = 0  # 统计准确率
# sum_mAR = 0  # 统计召回率
# sum_mAP = 0  # 统计精确率
# sum_F1 = 0  # 统计F1值
# cv = 10  # 交叉验证折数
# # 采用分层分组的形式(类似分层抽样),使每个分组中各类别的比例同整体数据中各类别的比例尽可能的相同
# kf = model_selection.StratifiedKFold(n_splits=cv, shuffle=True, random_state=2021)
# # 使用10折交叉验验证划分数据集，返回一个生成器对象（即索引）
# data_gen = kf.split(Train_X, Train_Y)
# for train_idx, test_idx in data_gen:
#     count += 1
#     print("\n第" + str(count) + "次:")
#     X_Train = Train_X[train_idx]  # 本次训练集
#     X_Test = Train_X[test_idx]  # 本次测试集
#     Y_Train = Train_Y[train_idx]  # 本次训练集标签
#     Y_Test = Train_Y[test_idx]  # 本次测试集标签
#     # 过采样,只对训练集进行过采样
#     sm = SMOTE(sampling_strategy='auto', random_state=2021)
#     X_resampled, y_resampled = sm.fit_resample(X_Train, Y_Train)
#     print(X_resampled.type())
#     print(y_resampled)
#     Bert.train_model(X_Train, args={'fp16': False})

# # 不加 这个if 会报错
# if __name__ == "__main__":
#     # 模型训练
#     model_train(model=model, TrainData=CorpusTrain)
#     # 模型评估
#     result, model_outputs, wrong_predictions = model_eval(model=model, TestData=CorpusTest)
#     # 模型精度指标
#     print("result :", result)
#     # 没太看懂 可能是特征向量？
#     print("model_outputs :", model_outputs)
#     # 目前输出是空值 不太确定
#     print("wrong_predictions :", wrong_predictions)
#     # 模型预测
#     predictions, raw_outputs = model_predict(model=model, textlist=list, labellist=listl)
#     # list(int) 每一个词的预测方法类型
#     print("predictions :", predictions)
#     # 和上面的model_outputs一致的
#     print("raw_outputs :", raw_outputs)
