const express= require("express");
const {database} = require("../config/helper");
const router = express.Router();


router.get("/:rocheta_id" , (req,res)=> {
    let id = req.params.rocheta_id;
    database.table("rocheta_details").withFields([
        "id",
        "drug",
        "description",
        "times"
    ]).filter({"rocheta_id": id}).getAll().then(result=> {
        if (result.length > 0) {
            res.send({success: true, result: result});
        } else {
            res.send({success: false, msg: "لا يوجد اي ادوية"});
        }
    }).catch(err => console.log(err))
});

router.put("/purchaseDrug" , (req,res)=> {
    const drug_id= req.body.drug_id;
    database.table("rocheta_details")
        .filter({id: drug_id})
        .update({purchased: 1})
        .then(result=> {
            if (result) {
                res.send({success: true});
            } else {
                res.send({success: false});
            }
            
        })
        .catch(error=> console.log(error));
});

router.put("/:drug_id" , (req, res)=> {
    const id= req.params.drug_id;
    const drugName= req.body.name;
    const drugDesc= req.body.desc;
    const times= req.body.times;

    database.table("rocheta_details")
    .filter({"id": id})
    .update({ "drug" : drugName, "description": drugDesc, "times": times})
    .then(success=> {
        if (success) {
            res.send({success: true});
        } else {
            res.send({success: false , msg: "حدث خطأ في الاتصال"});
        }
    }).catch(err=> console.log(err))
});

router.get("/currentDrugs/:user_id" , (req,res)=> {
    let user_id = req.params.user_id;
    database.table("rocheta_details").withFields([
        "id",
        "drug",
        "description",
        "times"
    ]).filter(
        {
            $and: [{"purchased": 1},
                {"rocheta_id = ": {$sql: ` ( SELECT id FROM rocheta WHERE patient_id = ${user_id} ORDER BY id DESC LIMIT 1) `}
                }]
    }).getAll().then(result=> {
        if (result.length > 0) {
            res.send({success: true, result: result});
        } else {
            res.send({success: false, msg: "لا يوجد اي ادوية"});
        }
    }).catch(err => console.log(err))
});


module.exports = router