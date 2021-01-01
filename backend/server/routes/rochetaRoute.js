const express= require("express");
const {database} = require("../config/helper");
const router = express.Router();

router.get("/drugs/:rocheta_id" , (req,res)=> {
    let id = req.params.rocheta_id;
    database.table("rocheta_details").withFields([
        "id",
        "drug",
        "description",
        "times"
    ]).filter({"rocheta_id": id , "purchased": 0}).getAll().then(result=> {
        if (result.length > 0) {
            res.send({success: true, result: result});
        } else {
            res.send({success: false, msg: "لا يوجد اي ادوية"});
        }
    }).catch(err => console.log(err))
});

router.get("/user/:user_id" , (req,res)=> {
    let id = req.params.user_id;
    database.table("rocheta").join([{
        table: "users",
        on: "users.id = rocheta.doctor_id"
    }]).withFields([
        "rocheta.id",
        "date",
        "CONCAT(users.firstname , ' ' , users.lastname) as doctor",
    ]).filter({"rocheta.patient_id": id}).sort({"rocheta.id": -1}).limit(1).get().then(result=> {
        if (result) {
            res.send({success: true, id: result.id, doctor: result.doctor, date: `${result.date.getFullYear()}-${result.date.getMonth()+1}-${result.date.getDate()}`})
        } else {
            res.send({success: false, msg: "لا يوجد اي روشيته لهذا المستخدم"})
        }
    }).catch(err => {
        res.send({success: false, msg: "حدث خطأ في الاتصال"})
        console.log(err)
    })
});

router.get("/user/allRochetas/:user_id" , (req,res)=> {
    let id = req.params.user_id;
    database.table("rocheta").join([{
        table: "users",
        on: "users.id = rocheta.doctor_id"
    }]).withFields([
        "rocheta.id",
        "date",
        "CONCAT(users.firstname , ' ' , users.lastname) as doctor",
    ]).filter({"rocheta.patient_id": id}).sort({"rocheta.id": -1}).getAll().then(result=> {
        if (result.length) {
            for (let i = 0; i < result.length; i++) {
                result[i].date = `${result[i].date.getFullYear()}-${result[i].date.getMonth()+1}-${result[i].date.getDate()}  ${result[i].date.getHours()}:${result[i].date.getMinutes()}`;  
            }
            res.send({success: true , result})
        } else {
            res.send({success: false, msg: "لا يوجد اي روشيته لهذا المستخدم"})
        }
    }).catch(err => {
        res.send({success: false, msg: "حدث خطأ في الاتصال"})
        console.log(err)
    })
});

router.delete("/:rocheta_id" , (req, res)=>{
    const id = req.params.rocheta_id;
    database.table("rocheta").filter({"id": id}).remove().then(success=> {
        if (success) {
            res.send({success: true});
        } else {
            res.send({success: false , msg: "حدث خطأ في الاتصال"});
        }
    }).catch(err=> console.log(err))
})

router.get("/doctor/:doctor_id" , (req,res)=> {
    let id = req.params.doctor_id;
    database.table("rocheta").join([{
        table: "users",
        on: "users.id = rocheta.patient_id"
    }]).withFields([
        "rocheta.id",
        "date",
        "CONCAT(users.firstname , ' ' , users.lastname) as patient",
    ]).filter({"rocheta.doctor_id": id}).sort({"rocheta.id": -1}).getAll().then(result=> {
        if (result.length > 0) {
            for (let i = 0; i < result.length; i++) {
                result[i].date = `${result[i].date.getFullYear()}-${result[i].date.getMonth()+1}-${result[i].date.getDate()}  ${result[i].date.getHours()}:${result[i].date.getMinutes()}`;  
            }
            res.send({
                success: true,
                result
            })
        } else {
            res.send({
                success: false,
                msg: "لا يوجد اي روشيتا"
            })
        }
    }).catch(err => console.log(err))
});

router.post("/addNewRocheta" , (req , res)=> {
    const doctor_id = req.body.doctor_id;
    const patient_id = req.body.patient_id;
    const date = new Date();
    const notes = req.body.notes;
    database.table("rocheta").insert({
        doctor_id: doctor_id,
        patient_id: patient_id,
        date: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}-${date.getHours()}-${date.getMinutes()}`,
        notes: notes 
    }).then(id=> {
        if (id) {
            res.send({success: true , id: id});
        } else {
            res.send({success: false , msg: "حدث خطأ في الاتصال"});
        }
    }).catch(err=> console.log(err));
})

router.post("/addNewDrugs" , (req , res)=> {
    const drug = req.body.drug;
    const drug_desc = req.body.drug_desc;
    const times = req.body.times;
    const rocheta_id = req.body.rocheta_id;

    database.table("rocheta_details").insert({
        drug: drug,
        description: drug_desc,
        times: times,
        rocheta_id: rocheta_id 
    }).then(id=> {
        if (id) {
            res.send({success: true});
        } else {
            res.send({success: false , msg: "حدث خطأ في الاتصال"});
        }
    }).catch(err=> console.log(err));
})


module.exports = router