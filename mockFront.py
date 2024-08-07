from flask import Flask, request
import requests

app = Flask(__name__)

server_url = 'http://localhost:8080'

@app.route('/password/reset')
def password_reset():
    token = request.args.get('token')
    user_id = request.args.get('id')
    new_password = input('Enter new password: ')
    resp = requests.post(f'{server_url}/user/password/{user_id}/recover/{token}', json={'password': new_password})
    if resp.status_code != 200:
        print('Password reset failed!')
        return 'Password reset failed!'
    else:
        print('Password reset successful!')
        return 'Password reset successful!'

@app.route('/auth/validate-account')
def validate_account():
    token = request.args.get('token')
    user_id = request.args.get('id')
    resp = requests.post(f'{server_url}/auth/user/{user_id}/validation/{token}')
    if resp.status_code != 200:
        print('Account validation failed!')
        return 'Account validation failed!'
    else:
        print('Account validation successful!')
        return 'Account validation successful!'

if __name__ == '__main__':
    app.run(port=4200)