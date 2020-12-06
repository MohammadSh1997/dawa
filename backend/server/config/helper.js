const mysqli = require("mysqli");

let conn = new mysqli({
    host : 'localhost' , //  IP/domain name  
    post : 3306 , // Port, default 3306  
    user : "root" , // Username  
    passwd : "" , // password  
    db : 'dawa' //You  can specify the database or not [optional]  
})

let db = conn.emit(false , "");
module.exports = {
    database: db
}
