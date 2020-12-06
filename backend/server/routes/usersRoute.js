const express = require("express");
const {database} = require("../config/helper");
const bcrypt = require("bcrypt");
const router = express.Router();


router.post("/register" , async (req , res)=> {
    const firstname = req.body.firstname;
    const lastname = req.body.lastname;
    const email = req.body.email;
    const password = req.body.password;
    const phone = req.body.phone;
    const type = req.body.type;

    const salt = await bcrypt.genSalt();

    await database.table("users").insert({
        firstname: firstname,
        lastname: lastname,
        email: email,
        password: password,
        phone: phone,
        type: type
    }).then(id => {
        res.send({success: true})
    }).catch(error=> {
        res.send({success: false , msg: error.message})
    })
})

router.post("/login" , (req , res)=> {
    const email= req.body.email || "";
    const password= req.body.password || "";

    database.table("users").withFields([
        "id",
        "firstname",
        "lastname",
        "password",
        "email",
        "phone",
        "type"
    ]).filter({"email": email})
        .get()
        .then(async user=> {
            if (user) {
                try {
                    if (password === user.password) {
                        res.send({success: true, id: user.id , firstname: user.firstname , lastname: user.lastname , email: user.email , phone: user.phone , type: user.type})
                    } else {
                        res.send({success: false , msg: "كلمة السر غير صحيحة"})
                    }
                } catch (error) {
                    res.send({success: false , msg: "حدث خطأ في الاتصال"})
                }
            } else {
                res.send({success: false , msg: "البريد الالكتروني غير موجود"})
            }
        }).catch(err=> console.log(err))
})

router.get("/getUser/:email" , (req , res)=> {
    const email = req.params.email;
    database.table("users").withFields([
        "id"
    ]).filter({"email": email}).get().then(result=> {
        if (result) {
            res.send({success: true , id: result.id})
        } else {
            res.send({success: false , msg: "البريد الالكتروني غير مستخدم"})
        }
    }).catch(err=> {
        console.log(err);
        res.send({success: false , msg: "حدث خطأ في الاتصال"});
    });
})

module.exports = router