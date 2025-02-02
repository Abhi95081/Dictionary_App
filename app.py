from flask import Flask, render_template, request, jsonify
import requests

app = Flask(__name__)

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/search', methods=['POST'])
def search_word():
    word = request.json.get('word', '')
    if not word:
        return jsonify({'error': 'Please enter a word'})
    
    # Using the Free Dictionary API
    api_url = f'https://api.dictionaryapi.dev/api/v2/entries/en/{word}'
    try:
        response = requests.get(api_url)
        if response.status_code == 200:
            data = response.json()
            if data and len(data) > 0:
                meanings = []
                for meaning in data[0]['meanings']:
                    part_of_speech = meaning['partOfSpeech']
                    definitions = [d['definition'] for d in meaning['definitions']]
                    meanings.append({
                        'partOfSpeech': part_of_speech,
                        'definitions': definitions
                    })
                return jsonify({
                    'word': data[0]['word'],
                    'meanings': meanings
                })
        return jsonify({'error': 'Word not found'})
    except Exception as e:
        return jsonify({'error': 'An error occurred while fetching the meaning'})

if __name__ == '__main__':
    app.run(debug=True)
