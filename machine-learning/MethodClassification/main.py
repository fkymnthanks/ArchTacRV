from sklearn.feature_extraction.text import TfidfVectorizer

from Detection.classifier import Base_Detection

if __name__ == "__main__":
    tactic_classifier = Base_Detection.classifierDict["AdaBoost"]
    tactic_detection = Base_Detection.TacticDetection(TfidfVectorizer(max_features=5000), tactic_classifier)
    corpus = Base_Detection.data_process("ping_echo_train")
    corpus_X_Vector, corpus_Y_Vector = tactic_detection.vectorize(corpus, "text_final", "label")
    tactic_detection.n_fold_evaluate(corpus_X_Vector, corpus_Y_Vector, k_neighbours=3, cv=10)
