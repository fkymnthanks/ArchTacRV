from collections import Counter

from sklearn.preprocessing import LabelEncoder
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn import naive_bayes
from ..buffer import filepath, namedata_filepath
from ..buffer import data_process
from ..evaluate import evaluate
from sklearn.model_selection import cross_val_score
from Detection.feature.tf_iwf import Tfiwf
import numpy as np

# 确保每次生成的随即数都相同
np.random.seed(500)

# train_file_path = namedata_filepath.get_authorize_name_data()
train_file_path = filepath.get_fifo_train()
# test_file_path = namedata_filepath.get_cache_name_data_test()
CorpusTrain = data_process.read_csv(train_file_path)
# CorpusTest = data_process.read_csv(test_file_path)
# 数据处理
CorpusTrain = data_process.base_tokenizer(CorpusTrain)
# CorpusTest = data_process.base_tokenizer(CorpusTest)
Train_X = CorpusTrain['text_final']
Train_Y = CorpusTrain['label']
print(Counter(Train_Y))
# Test_X = CorpusTest['text_final']
# Test_Y = CorpusTest['label']
Encoder = LabelEncoder()
Train_Y = Encoder.fit_transform(Train_Y)
# Test_Y = Encoder.fit_transform(Test_Y)
# tf-idf向量化
tfidf_vector = TfidfVectorizer(max_features=5000)
tfidf_vector.fit(Train_X)
# tfidf_vector.fit(Test_X)
Train_X_Tfidf = tfidf_vector.transform(Train_X)
# print(tfidf_vector.vocabulary_)
# print(Train_X_Tfidf)
# Test_X_Tfidf = tfidf_vector.transform(Test_X)
# # tf-iwf向量化
# tfiwf_vector = Tfiwf(Train_X)
# result1 = tfiwf_vector.get_tfiwf(Train_X)
# Train_X_Tfiwf = tfiwf_vector.transform(Train_X)
# ------贝叶斯------
Bayes = naive_bayes.MultinomialNB()
evaluate.n_fold_evaluate(Bayes, Train_X_Tfidf, Train_Y, k_neighbours=3, cv=10)
