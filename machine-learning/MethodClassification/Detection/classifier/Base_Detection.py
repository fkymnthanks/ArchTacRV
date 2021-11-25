from imblearn.over_sampling import SMOTE
from sklearn import model_selection, naive_bayes, svm
from sklearn.ensemble import AdaBoostClassifier, GradientBoostingClassifier, RandomForestClassifier
from sklearn.metrics import confusion_matrix, accuracy_score, recall_score, precision_score, f1_score
from sklearn.tree import DecisionTreeClassifier

from ..buffer.filepath import get_tactic_data
from ..buffer.data_process import read_csv, base_tokenizer
from sklearn.preprocessing import LabelEncoder

classifierDict = {"AdaBoost": AdaBoostClassifier(DecisionTreeClassifier(max_depth=5), n_estimators=40),
                  "Bayes": naive_bayes.MultinomialNB(),
                  "GBDT": GradientBoostingClassifier(loss='deviance', learning_rate=1, n_estimators=5, subsample=1

                                                     , min_samples_split=2, min_samples_leaf=1, max_depth=2

                                                     , init=None, random_state=None, max_features=None

                                                     , verbose=0, max_leaf_nodes=None, warm_start=False

                                                     ),
                  "RF": RandomForestClassifier(),
                  "SVM": svm.SVC(C=1.0, kernel='linear', degree=3, gamma='auto')}


def data_process(tactic_type):
    """
    读取.csv文件并进行数据处理
    :param tactic_type: 数据文件字典的Key
    :return: 返回处理后的策略数据，一个dataFrame
    """
    data_filepath = get_tactic_data(tactic_type)
    Corpus = read_csv(data_filepath)
    Corpus = base_tokenizer(Corpus)
    return Corpus


class TacticDetection:
    def __init__(self, vectorization_tool, classifier):
        self.vectorization_tool = vectorization_tool  # 特征向量化工具
        self.classifier = classifier  # 进行策略分类所用的分类器

    def vectorize(self, corpus, text, label):
        """
        数据特征向量化处理
        :param corpus: 语料数据
        :param text: 文本列名
        :param label: 标签列名
        :return:
        """
        Corpus_X = corpus[text]
        Corpus_Y = corpus[label]
        self.vectorization_tool.fit(Corpus_X)
        Corpus_X_Vector = self.vectorization_tool.transform(Corpus_X)
        Encoder = LabelEncoder()
        Corpus_Y_Vector = Encoder.fit_transform(Corpus_Y)
        return Corpus_X_Vector, Corpus_Y_Vector

    def n_fold_evaluate(self, train_data, train_label, k_neighbours, cv=10):
        """
        n折交叉验证
        :param train_data: 文本数据
        :param train_label: 分类标签
        :param k_neighbours: 进行过采样时的neighbour数
        :param cv: 交叉验证折数
        """
        count = 0  # 统计循环次数
        accuracy = 0  # 统计准确率
        sum_mAR = 0  # 统计召回率
        sum_mAP = 0  # 统计精确率
        sum_F1 = 0  # 统计F1值
        # 采用分层分组的形式(类似分层抽样),使每个分组中各类别的比例同整体数据中各类别的比例尽可能的相同
        kf = model_selection.StratifiedKFold(n_splits=cv, shuffle=True, random_state=2021)
        # 使用10折交叉验验证划分数据集，返回一个生成器对象（即索引）
        data_gen = kf.split(train_data, train_label)
        for train_idx, test_idx in data_gen:  # 循环10次
            count += 1
            print("\n第" + str(count) + "次:")
            X_Train = train_data[train_idx]  # 本次训练集
            X_Test = train_data[test_idx]  # 本次测试集
            Y_Train = train_label[train_idx]  # 本次训练集标签
            Y_Test = train_label[test_idx]  # 本次测试集标签
            # 过采样,只对训练集进行过采样
            sm = SMOTE(sampling_strategy='auto', random_state=2021, k_neighbors=k_neighbours)
            X_resampled, y_resampled = sm.fit_resample(X_Train, Y_Train)
            self.classifier.fit(X_resampled, y_resampled)  # 训练本次模型
            predictions_NB = self.classifier.predict(X_Test)  # 预测
            # 在混淆矩阵中,每一行之和表示该类别的真实样本数量,每一列之和表示被预测为该类别的样本数量,第i行j列表示应该为i类被预测为j类的数量
            con_matrix = confusion_matrix(Y_Test, predictions_NB)
            print(con_matrix)  # 输出混淆矩阵
            print("准确率：%s" % accuracy_score(Y_Test, predictions_NB))  # 输出准确率
            recall_every_class = recall_score(Y_Test, predictions_NB, average=None)
            recall_macro = recall_score(Y_Test, predictions_NB, average='macro')
            recall_micro = recall_score(Y_Test, predictions_NB, average='micro')
            recall_weighted = recall_score(Y_Test, predictions_NB, average='weighted')
            print("召回率：%s %s %s %s" % (recall_every_class, recall_macro, recall_micro, recall_weighted))  # 输出召回率
            precision_every_class = precision_score(Y_Test, predictions_NB, average=None)
            precision_macro = precision_score(Y_Test, predictions_NB, average='macro')
            precision_micro = precision_score(Y_Test, predictions_NB, average='micro')
            precision_weighted = precision_score(Y_Test, predictions_NB, average='weighted')
            print(
                "精确率：%s %s %s %s" % (
                    precision_every_class, precision_macro, precision_micro, precision_weighted))  # 输出精确率
            f1_every_class = f1_score(Y_Test, predictions_NB, average=None)
            f1_macro = f1_score(Y_Test, predictions_NB, average='macro')
            f1_micro = f1_score(Y_Test, predictions_NB, average='micro')
            f1_weighted = f1_score(Y_Test, predictions_NB, average='weighted')
            print("F1值：%s %s %s %s" % (f1_every_class, f1_macro, f1_micro, f1_weighted))  # 输出F1值
            accuracy += accuracy_score(predictions_NB, Y_Test)
            sum_mAP = sum_mAP + precision_macro
            sum_mAR = sum_mAR + recall_macro
            sum_F1 = sum_F1 + f1_macro
        print("\n")
        print("平均准确率：")
        print(accuracy / count)
        print("平均召回率：")
        print(sum_mAR / count)
        print("平均精确率：")
        print(sum_mAP / count)
        print("平均F1值：")
        print(sum_F1 / count)
