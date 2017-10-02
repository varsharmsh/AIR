import sys
import nltk
import string

if len(sys.argv) < 2:
    sys.exit("Error : Usage python preprocess.py <filename>")

def preprocess(tokens):
    stopwords = set(nltk.corpus.stopwords.words('english'))
    stemmer = nltk.stem.PorterStemmer()
    #remove punctuation
    tokens = map(lambda x: x.translate(None,string.punctuation),tokens)
    tokens = filter(lambda x: x, tokens)
    #convert to lowercase
    tokens = map(lambda x: x.lower(), tokens)
    #remove stopwords
    tokens = filter(lambda x: x not in stopwords,tokens)
    #stem tokens
    tokens = map(lambda x: stemmer.stem(x), tokens)
    return tokens

if __name__ == "__main__":
    with open(sys.argv[1]) as file_handle:
        text = file_handle.read()
        tokens = nltk.word_tokenize(text)
        print("Number of tokens without preprocessing : ",len(set(tokens)))
        preprocessed_tokens = preprocess(tokens)
        print("Number of tokens after preprocessing : ",len(set(preprocessed_tokens)))
        #print(preprocessed_tokens)
