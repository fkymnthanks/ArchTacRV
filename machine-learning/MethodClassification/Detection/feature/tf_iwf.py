import numpy as np
import math
import scipy.sparse as sp


class Tfiwf:
    '''
    tf-iwf 算法
    '''

    def __init__(self, lines):
        self.iwf = dict()
        self.wordNum = 0
        self.wordDict = dict()
        self.median_iwf = 0
        self.__build_iwf(lines)

    def __get_tf(self, strs):
        tf_dict = {}
        for line in strs:
            total_word_line = len(line)
            for word in eval(line):
                if word not in tf_dict:
                    tf_dict[word] = 1
                else:
                    tf_dict[word] = tf_dict[word] + 1
            for k, v in tf_dict.items():
                tf_dict[k] = v / total_word_line
        return tf_dict

    '''
        根据输入训练集训练，初始化tf-iwf
    '''

    def __build_iwf(self, lines):

        for line in lines:
            for word in eval(line):
                if word not in self.iwf:
                    self.iwf[word] = 1
                    self.wordDict[word] = self.wordNum
                    self.wordNum = self.wordNum + 1
                else:
                    self.iwf[word] = self.iwf[word] + 1
        total_word_lines = len(self.iwf.values())
        values = []
        for k, v in self.iwf.items():
            self.iwf[k] = math.log((total_word_lines + 1) / (v + 1), 10)
            values.append(math.log((total_word_lines + 1) / (v + 1), 10))
        self.median_iwf = np.median(values)

    '''
        输入测试集，得到测试集每个不同的词的tfiwf值的字典
    '''

    def get_tfiwf(self, strs):
        result = dict()
        tf_dict = self.__get_tf(strs)
        for line_words in strs:
            for word in eval(line_words):
                if word not in self.iwf.keys():
                    result[word] = tf_dict[word] * self.median_iwf
                else:
                    result[word] = tf_dict[word] * self.iwf[word]
        return result

    '''
        根据输入的测试集生成文本集的稀疏矩阵
    '''

    def transform(self, raw_documents):

        _, X = self._count_vocab(raw_documents, fixed_vocab=True)
        return X

    def _count_vocab(self, raw_documents, fixed_vocab):
        """Create sparse feature matrix, and vocabulary where fixed_vocab=False
        """
        vocabulary = self.iwf
        vocabulary2 = self.wordDict

        j_indices = []
        indptr = []

        values = []
        indptr.append(0)
        for doc in raw_documents:
            feature_counter = {}
            for feature in eval(doc):
                try:
                    feature_idx = vocabulary2[feature]
                    feature_counter[feature_idx] = math.fabs(vocabulary[feature])
                except KeyError:
                    # Ignore out-of-vocabulary items for fixed_vocab=True
                    continue

            j_indices.extend(feature_counter.keys())
            values.extend(feature_counter.values())
            indptr.append(len(j_indices))
        if indptr[-1] > np.iinfo(np.int32).max:  # = 2**31 - 1
            indices_dtype = np.int64
        else:
            indices_dtype = np.int32
        j_indices = np.asarray(j_indices, dtype=indices_dtype)
        indptr = np.asarray(indptr, dtype=indices_dtype)
        values = np.asarray(values, dtype=np.float32)

        X = sp.csr_matrix((values, j_indices, indptr),
                          shape=(len(indptr) - 1, len(vocabulary)),
                          dtype=np.float32)
        X.sort_indices()
        return vocabulary, X
