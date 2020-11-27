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
            res.send({success: false, msg: "no rocheta found"});
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
            res.send({success: false, msg: "no rocheta found"})
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
        date: `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`,
        notes: notes 
    }).then(id=> {
        if (id) {
            res.send({success: true , id: id});
        } else {
            res.send({success: false , msg: "server error"});
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
            res.send({success: false , msg: "server error"});
        }
    }).catch(err=> console.log(err));
})

module.exports = router