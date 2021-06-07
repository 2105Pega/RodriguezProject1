let token;
// async function login(username,password){
//     url = 'http://localhost:8080/App/api/login'
//     loginData = {
//         "username": username.toString(),
//         "password": password.toString()
//     }
//     await fetch(url,{
//         method: 'POST',
//         headers: {
//             'Content-type': 'application/json'
//         },
//         body: JSON.stringify(loginData)
//     })
//     .then(response => response.text())
//     .then(data => {
//         token = data;
//         console.log(token);
//     })
//     .then(renderAccounts())
//     .catch((error) =>{
//         console.log("Error: ", error);
//     });
// }
async function login(username,password){
    url = 'http://localhost:8080/App/api/login'
    loginData = {
        "username": username.toString(),
        "password": password.toString()
    }
    const data = await fetch(url,{
        method: "Post",
        headers:{
            'Content-type':'application/json'
        },
        body: JSON.stringify(loginData)
    });
    let responseData = await data.text();
    return responseData;
}
async function register(username,password){
    url = 'http://localhost:8080/App/api/register'
    loginData = {
        "username": username,
        "password": password
    }
    const data = await fetch(url,{
        method: 'POST',
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify(loginData)
    });
    let responseData = await data.status;
    return;
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
    let responseData = await response.json;
    return responseData;
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
document.getElementById("login_button").addEventListener("click",function(event){
    event.preventDefault();
    username = document.getElementById("username").value;
    password = document.getElementById("password").value;
    console.log(username)
    console.log(password);
    login(username,password).then((data)=> {token = data}).then(()=>{renderAccounts()});
}, false);
var registrationForm ="<div id=\"spa\" class=\"valign-wrapper\"><div class=\"row\">"
                        + "<form id=\"login\" class=\"col s12\">"
                        + "<div class=\"row\">"
                        + "<div class=\"input-field col s6\"><input placeholder=\"Username\" id=\"username\" type=\"text\""
                        + "class=\"validate\"><label for=\"first_name\">Username</label></div>"
                        + "<div class=\"input-field col s6\"><input id=\"password\" type=\"text\" class=\"validate\">"
                        + "<label for=\"password\">Password</label></div>"
                        + "</div><button type=\"button\" class=\"btn waves-effect waves-light\"" 
                        + "id=\"login_button\">Submit<i class=\"material-icons right\">send</i></button>"
                        + "<button type=\"button\" class=\"btn waves-effect waves-light\" id=\"register_button\">Register</button>"
                        + "</form> </div>"
async function renderAccounts(){
    let data = await getAccounts()
    console.log(data);
}