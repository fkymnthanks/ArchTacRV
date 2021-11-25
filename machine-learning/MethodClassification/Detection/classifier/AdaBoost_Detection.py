from collections import Counter

from sklearn.preprocessing import LabelEncoder
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn import naive_bayes
from Detection.buffer import filepath, namedata_filepath
from Detection.buffer import data_process
from Detection.evaluate import evaluate
from Detection.feature.tf_iwf import Tfiwf
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import AdaBoostClassifier
import numpy as np



# 确保每次生成的随即数都相同
np.random.seed(500)

# train_file_path = namedata_filepath.get_authorize_name_data()
train_file_path = filepath.get_authenticate_train()
CorpusTrain = data_process.read_csv(train_file_path)
# 数据处理
CorpusTrain = data_process.base_tokenizer(CorpusTrain)
Train_X = CorpusTrain['text_final']
Train_Y = CorpusTrain['label']
Encoder = LabelEncoder()
Train_Y = Encoder.fit_transform(Train_Y)
print(Counter(Train_Y))

# # tf-idf向量化
# tfidf_vector = TfidfVectorizer(max_features=5000)
# tfidf_vector.fit(Train_X)
# Train_X_Tfidf = tfidf_vector.transform(Train_X)
# tf-iwf向量化
tfiwf_vector = Tfiwf(Train_X)
result1 = tfiwf_vector.get_tfiwf(Train_X)
Train_X_Tfiwf = tfiwf_vector.transform(Train_X)
# ------AdaBoost------
AdaBoost = AdaBoostClassifier(DecisionTreeClassifier(max_depth=5), n_estimators=40)
evaluate.n_fold_evaluate(AdaBoost, Train_X_Tfiwf, Train_Y, k_neighbours=3, cv=10)
