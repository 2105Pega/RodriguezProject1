let token;
async function login(username,password){
    url = 'http://localhost:8080/App/api/login'
    loginData = {
        "username": username.toString(),
        "password": password.toString()
    }
    fetch(url,{
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.text())
    .then(data => {
        token = data;
        console.log(token);
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
async function register(username,password){
    url = 'http://localhost:8080/App/api/register'
    loginData = {
        "username": username,
        "password": password
    }
    fetch(url,{
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(loginData)
    })
    .then(response => response.text)
    .then(data => {
        token += response;
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
async function getAccounts(){
    url = 'http://localhost:8080/App/api/home'
    console.log(token);
    const response = await fetch(url,{
        method: 'GET',
        headers:{
            'Bearer': token,
        }
    });
    return response.json;
}
async function getApplication(id){
    url = 'http://localhost:8080/App/api/application/' + id;
    const response = await fetch(url,{
        method: 'GET',
        headers:{
            'Bearer': token
        }
    });
    return response.json;
}
async function approveApplication(id){
    url = 'http://localhost:8080/App/api/application/approve/' + id;
    const response = await fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        }
    });
    return response.status.toString();
}
async function denyApplication(id){
    url = 'http://localhost:8080/App/api/application/deny/' + id;
    const response = await fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        }
    });
    return response.status.toString();
}
async function newApplication(id){
    url = 'http://localhost:8080/App/api/application/new/' + id;
    fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        }
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
async function getAccount(id){
    url = 'http://localhost:8080/App/api/account/' + id;
    const response = await fetch(url,{
        method: 'GET',
        headers:{
            'Bearer': token
        }
    });
    return response.json;
}
async function makeDeposit(id,amount){
    url = 'http://localhost:8080/App/api/account/' + id + '/deposit';
    data = { "amount" : amount }
    fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        },
        body: JSON.stringify(data)
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
async function makeWithdrawl(id,amount){
    url = 'http://localhost:8080/App/api/account/' + id + '/withdraw';
    data = { "amount" : amount }
    fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        },
        body: JSON.stringify(data)
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
async function closeAccount(id){
    url = 'http://localhost:8080/App/api/close/' + id;
    data = { "amount" : amount }
    fetch(url,{
        method: 'POST',
        headers:{
            'Bearer': token
        },
        body: JSON.stringify(data)
    })
    .catch((error) =>{
        console.log("Error: ", error);
    });
}
M.AutoInit();
document.getElementById("login_button").addEventListener("click", function(event){
    event.preventDefault();
    username = document.getElementById("username").value;
    password = document.getElementById("password").value;
    login(username,password);
});