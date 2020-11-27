const express= require("express");
const {database} = require("../config/helper");
const router = express.Router();

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
            res.send({success: false, msg: "no drugs found"});
        }
    }).catch(err => console.log(err))
});
module.exports = router