import requests

res = requests.post('http://localhost:8080/api/auth/login', json={'email':'admin@clinic.com','password':'password'})
token = res.json().get('token')

res2 = requests.get('http://localhost:8080/api/pharmacy/prescriptions/pending', headers={'Authorization': 'Bearer ' + token})
print("STATUS:", res2.status_code)
print("RESP:", res2.text)
