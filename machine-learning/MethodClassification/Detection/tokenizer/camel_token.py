from nltk.tokenize import WordPunctTokenizer

'''
英文分词器,用于将原始数据进行驼峰分词
'''


def camel_token(content):
    word_list = WordPunctTokenizer().tokenize(content)  # 按分隔符分词
    result = ""
    for word in word_list:
        if not word.isalpha():
            continue
        split_word_list = camel_case_token(word)
        result += split_word_list
    return result


def camel_case_token(name_string):
    str_list = list(name_string)
    index = []
    for i in range(len(str_list)):
        if 'A' <= str_list[i] <= 'Z':
            index.append(i)
    for i in range(len(index)):
        if i == 0: continue
        index[i] = index[i] + i
    for i in index:
        str_list.insert(i, '*')
    name_string = ''.join(str_list)
    result_string_list = name_string.split('*')
    result_string = ''
    for i in range(len(result_string_list)):
        if len(result_string_list[i]) < 2:
            continue
        result_string += result_string_list[i]
        result_string += ' '
    return result_string
