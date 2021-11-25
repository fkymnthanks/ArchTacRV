from simpletransformers.classification import ClassificationModel
from sklearn.metrics import accuracy_score
from sklearn.metrics import confusion_matrix
# from sklearn.ensemble import RandomForestClassifier
import joblib


# Dir可以为这个参数可以不传 不传会创建一个新的模型 传了表示加载之前的模型
# labelnum 表示总共有几种类型的方法
def model_init(Dir=None, labelnum=None):
    if Dir == None:
        Dir = "bert-base-uncased"
    model = ClassificationModel("bert", Dir, num_labels=labelnum,
                                args={"reprocess_input_data": True, "overwrite_output_dir": True})
    return model


# model为model_init的返回值
# TrainData 为训练集 ,格式和之前学长代码中的CorpusTrain一致即可
def model_train(model=None, TrainData=None):
    model.train_model(TrainData, args={'fp16': False})


# model为model_init的返回值
# TestData 为训练集 ,格式和之前学长代码中的CorpusTest一致即可
def model_eval(model=None, TestData=None):
    result, model_outputs, wrong_predictions = model.eval_model(TestData, acc=accuracy_score)
    return result, model_outputs, wrong_predictions


# model为model_init的返回值
# textlist为一个str的list,内容即为分词后的测试集的词
# labellist为一个int 的list,内容为分词后测试集每个词的方法类型
def model_predict(model=None, textlist=None, labellist=None):
    predictions, raw_outputs = model.predict(textlist)
    print(confusion_matrix(labellist, predictions))

    print("准确率：")
    print(accuracy_score(predictions, labellist))

    M = confusion_matrix(labellist, predictions)
    # print(M)
    n = len(M)

    recallsum = 0
    precisionsum = 0

    for i in range(n):
        rowsum, colsum = sum(M[i]), sum(M[r][i] for r in range(n))
        if (rowsum == 0):
            rowsum = 1
        if (colsum == 0):
            colsum = 1
        try:
            recallsum = recallsum + (M[i][i] / float(rowsum))
            precisionsum = precisionsum + (M[i][i] / float(colsum))
        except ZeroDivisionError:
            print('precision: %s' % 0, 'recall: %s' % 0)

    mAR = recallsum / n
    mAP = precisionsum / n

    print('mAP: %s' % mAP)
    print('mAR: %s' % mAR)
    print('F1: %s' % (2 * (mAP * mAR) / (mAP + mAR)))
    return predictions, raw_outputs
