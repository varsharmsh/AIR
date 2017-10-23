from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import PorterStemmer
import json
import math

stop_words = set(stopwords.words('english'))
index = {}
normalizing_factors = {}
total_docs = 0
doc_freq = "doc_freq"
posting_list = "posting_list"

def filter_words(content):
    """Preprocesses the passed content by removing stop words and stemming each word using porter stemmer"""
    words = word_tokenize(content)
    words = [word for word in words if word not in stop_words]
    stemmer = PorterStemmer()
    words = [stemmer.stem(word) for word in words]
    return words

def add_doc_to_index(content, doc_id):
    """adds the given content and docId to the index being built"""
    words = filter_words(content)
    unique_words = {}
    for word in words:
        if word not in unique_words:
            unique_words[word] = 0

        unique_words[word] += 1
        
        if word not in index:
            index[word] = {doc_freq: 0, posting_list: {}}

        if doc_id not in index[word][posting_list]:
            index[word][posting_list][doc_id] = 0
        
        index[word][posting_list][doc_id] += 1

    normalizing_factor = 0
    temp = 0

    for word in unique_words.keys():
        index[word][doc_freq] += 1
        temp += unique_words[word] ** 2
    
    normalizing_factor = math.sqrt(temp)
    normalizing_factors[doc_id] = normalizing_factor
    

def generate_index(file_name):
    """Reads the file and parses it according to .W rule of assignment4 and passes docId and the corresponding content to add_word_to_index"""
    with open(file_name, 'r') as input_file:
        doc_id = 0
        started = False
        content = ""
        for line in input_file.readlines():
            if line[0:2] == '.W':
                started = True
                doc_id += 1
            
            elif line[0:2] == '.I':
                if started:
                    add_doc_to_index(content, doc_id)
                started = False
                content = ""
            
            else:
                if started:
                    content += line
        
        add_doc_to_index(content, doc_id)
        global total_docs
        total_docs = doc_id

def find_similarity(words):
    score = {}
    
    for word in words:
        try:
            idf = math.log10(total_docs / index[word][doc_freq])
            for doc_id in index[word][posting_list].keys():
                if doc_id not in score:
                    score[doc_id] = 0
                ## check this part
                tf = 1 + math.log10(index[word][posting_list][doc_id])
                score[doc_id] +=  tf * idf
        except:
            continue
    
    for key in score.keys():
        score[key] = score[key] / normalizing_factors[key]

    score = sorted(score.items(), key = lambda e : e[1], reverse=True)
    print(score[0: min(10, len(score))]) 

def ask_query():
    """waits for input form user and finds 10 most relevant documents"""
    while True:
        query_string = input('query > ')
        words = filter_words(query_string)
        find_similarity(words)            

if __name__ == '__main__':
    print('Buiding index ......\n')
    generate_index('cran.all.1400')
    print('Done\n')
    ask_query()