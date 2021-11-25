from collections import Counter

from sklearn.preprocessing import LabelEncoder
from sklearn.feature_extraction.text import TfidfVectorizer
from Detection.buffer import filepath, namedata_filepath
from Detection.buffer import data_process
from Detection.evaluate import evaluate
from Detection.feature.tf_iwf import Tfiwf
from sklearn.ensemble import GradientBoostingClassifier
import numpy as np

# 确保每次生成的随即数都相同
np.random.seed(500)

# train_file_path = namedata_filepath.get_authorize_name_data()
train_file_path = filepath.get_fifo_train()
CorpusTrain = data_process.read_csv(train_file_path)
# 数据处理
CorpusTrain = data_process.base_tokenizer(CorpusTrain)
Train_X = CorpusTrain['text_final']
Train_Y = CorpusTrain['label']
Encoder = LabelEncoder()
Train_Y = Encoder.fit_transform(Train_Y)
print(Counter(Train_Y))

# tf-idf向量化
tfidf_vector = TfidfVectorizer(max_features=5000)
tfidf_vector.fit(Train_X)
Train_X_Tfidf = tfidf_vector.transform(Train_X)
# # tf-iwf向量化
# tfiwf_vector = Tfiwf(Train_X)
# result1 = tfiwf_vector.get_tfiwf(Train_X)
# Train_X_Tfiwf = tfiwf_vector.transform(Train_X)
# ------GBDT------
GBDT = GradientBoostingClassifier(loss='deviance', learning_rate=1, n_estimators=5, subsample=1

                                  , min_samples_split=2, min_samples_leaf=1, max_depth=2

                                  , init=None, random_state=None, max_features=None

                                  , verbose=0, max_leaf_nodes=None, warm_start=False

                                  )
evaluate.n_fold_evaluate(GBDT, Train_X_Tfidf, Train_Y, k_neighbours=3, cv=10)
