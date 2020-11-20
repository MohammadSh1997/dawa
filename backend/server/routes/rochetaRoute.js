const express= require("express");
const {database} = require("../config/helper");
const router = express.Router();

router.get("/:rocheta_id" , (req,res)=> {
    let id = req.params.rocheta_id;
    database.table("rocheta_details").withFields([
        "drug",
        "description"
    ]).filter({"rocheta_id": id}).getAll().then(result=> {
        if (result) {
            res.send({result: result})
        } else {
            res.send("no rocheta found")
        }
    }).catch(err => console.log(err))
});

router.get("/user/:user_id" , (req,res)=> {
    let id = req.params.user_id;
    database.table("rocheta").join([{
        table: "users",
        on: "users.id = rocheta.doctor_id"
    }]).withFields([
        "date",
        "CONCAT(users.firstname , ' ' , users.lastname) as doctor",
    ]).filter({"rocheta.patient_id": id}).get().then(result=> {
        if (result) {
            res.send({result: result})
        } else {
            res.send("no rocheta found")
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