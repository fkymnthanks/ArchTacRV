import pandas as pd
from Detection.tokenizer.camel_token import camel_token
from nltk.corpus import wordnet as wn
from collections import defaultdict
from nltk.stem import WordNetLemmatizer
from nltk import pos_tag
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from sklearn.utils import shuffle


def read_csv(file_path):
    df = pd.read_csv(file_path, encoding='latin-1', engine='python')
    return df


def base_tokenizer(corpus_data):
    """
    提供基本的数据处理和分词功能，包括去除空行、驼峰分割、去除大小写、按动名词分词等
    """
    # 去除含有空缺值的行
    corpus_data['text'].dropna(axis=0, how="any", inplace=True)
    # 驼峰命名分割
    corpus_data['text'] = [camel_token(entry) for entry in corpus_data['text']]
    # 去除大小写
    corpus_data['text'] = [str(entry).lower() for entry in corpus_data['text']]
    tag_map = defaultdict(lambda: wn.NOUN)
    tag_map['J'] = wn.ADJ
    tag_map['V'] = wn.VERB
    tag_map['R'] = wn.ADV
    for index, entry in enumerate(list(corpus_data['text'])):
        final_words = []
        word_lemmatizer = WordNetLemmatizer()
        for word, tag in pos_tag(word_tokenize(entry)):
            if word not in stopwords.words('english') and word.isalpha():
                word_final = word_lemmatizer.lemmatize(word, tag_map[tag[0]])
                final_words.append(word_final)
        corpus_data.loc[index, 'text_final'] = str(final_words)
    corpus_data['text'].dropna(inplace=True)
    return corpus_data
