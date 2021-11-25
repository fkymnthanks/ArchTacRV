from sklearn import model_selection
from sklearn.metrics import confusion_matrix
from sklearn.metrics import accuracy_score, recall_score, precision_score, f1_score
from imblearn.over_sampling import SMOTE
from sklearn.preprocessing import label_binarize
import numpy as np


def n_fold_evaluate(model, train_data, train_label, k_neighbours, cv=10):
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
        model.fit(X_resampled, y_resampled)  # 训练本次模型
        predictions_NB = model.predict(X_Test)  # 预测
        # 在混淆矩阵中,每一行之和表示该类别的真实样本数量,每一列之和表示被预测为该类别的样本数量,第i行j列表示应该为i类被预测为j类的数量
        con_matrix = confusion_matrix(Y_Test, predictions_NB)
        # n = len(con_matrix)
        # recall_sum = 0
        # precision_sum = 0
        # for i in range(n):
        #     row_sum, col_sum = sum(con_matrix[i]), sum(con_matrix[r][i] for r in range(n))
        #     if row_sum == 0:
        #         row_sum = 1
        #     if col_sum == 0:
        #         col_sum = 1
        #     try:
        #         # 召回率表示的是样本中的正例有多少被预测正确
        #         recall_sum = recall_sum + (con_matrix[i][i] / float(row_sum))
        #         # 精确率表示的是表示的是预测为正的样本中有多少是真正的正样本
        #         precision_sum = precision_sum + (con_matrix[i][i] / float(col_sum))
        #     except ZeroDivisionError:
        #         print('precision: %s' % 0, 'recall: %s' % 0)
        # mAR = recall_sum / n
        # mAP = precision_sum / n
        # if mAR == 0 and mAP == 0:
        #     F1 = 0
        # else:
        #     F1 = (2 * (mAP * mAR) / (mAP + mAR))
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
            "精确率：%s %s %s %s" % (precision_every_class, precision_macro, precision_micro, precision_weighted))  # 输出精确率
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


def leave_one_evaluate(model, train_data, train_label):
    count = 0  # 统计循环次数
    accuracy = 0  # 统计准确率
    sum_mAR = 0  # 统计召回率
    sum_mAP = 0  # 统计精确率
    sum_F1 = 0  # 统计F1值
    loo = model_selection.LeaveOneOut()
    # 使用留一法划分数据集，返回一个生成器对象（即索引）
    data_gen = loo.split(train_data)
    for train_idx, test_idx in data_gen:  # 循环n次
        count += 1
        print("\n第" + str(count) + "次:")
        X_Train = train_data[train_idx]  # 本次训练集
        X_Test = train_data[test_idx]  # 本次测试集
        Y_Train = train_label[train_idx]  # 本次训练集标签
        Y_Test = train_label[test_idx]  # 本次测试集标签
        # 过采样,只对训练集进行过采样
        sm = SMOTE(sampling_strategy='auto', random_state=2021)
        X_resampled, y_resampled = sm.fit_resample(X_Train, Y_Train)
        model.fit(X_resampled, y_resampled)  # 训练本次模型
        predictions_NB = model.predict(X_Test)  # 预测
        # 在混淆矩阵中,每一行之和表示该类别的真实样本数量,每一列之和表示被预测为该类别的样本数量,第i行j列表示应该为i类被预测为j类的数量
        con_matrix = confusion_matrix(Y_Test, predictions_NB)
        n = len(con_matrix)
        recall_sum = 0
        precision_sum = 0
        for i in range(n):
            row_sum, col_sum = sum(con_matrix[i]), sum(con_matrix[r][i] for r in range(n))
            if row_sum == 0:
                row_sum = 1
            if col_sum == 0:
                col_sum = 1
            try:
                # 召回率表示的是样本中的正例有多少被预测正确
                recall_sum = recall_sum + (con_matrix[i][i] / float(row_sum))
                # 精确率表示的是表示的是预测为正的样本中有多少是真正的正样本
                precision_sum = precision_sum + (con_matrix[i][i] / float(col_sum))
            except ZeroDivisionError:
                print('precision: %s' % 0, 'recall: %s' % 0)
        mAR = recall_sum / n
        sum_mAR = sum_mAR + mAR
        mAP = precision_sum / n
        sum_mAP = sum_mAP + mAP
        if mAR == 0 and mAP == 0:
            F1 = 0
        else:
            F1 = (2 * (mAP * mAR) / (mAP + mAR))
        sum_F1 = sum_F1 + F1
        print(con_matrix)  # 输出混淆矩阵
        print("准确率：%s" % accuracy_score(predictions_NB, Y_Test))  # 输出准确率
        print("召回率：%s" % mAR)  # 输出召回率
        print("精确率：%s" % mAP)  # 输出精确率
        print("F1值：%s" % F1)  # 输出F1值
        accuracy += accuracy_score(predictions_NB, Y_Test)

    print("\n")
    print("平均准确率：")
    print(accuracy / count)
    print("平均召回率：")
    print(sum_mAR / count)
    print("平均精确率：")
    print(sum_mAP / count)
    print("平均F1值：")
    print(sum_F1 / count)
