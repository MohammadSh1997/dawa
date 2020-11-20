const express= require("express");
const bodyParser = require("body-parser");
const usersRoute= require("./routes/usersRoute");
const rochetaRoute= require("./routes/rochetaRoute");
require("dotenv").config();

const app= express();
const PORT= 8080;

app.use(express.json());
app.use(bodyParser.json())

app.use("/api/auth" , usersRoute);
app.use("/api/rocheta" , rochetaRoute);

app.listen(PORT , ()=> {console.log(`server running on https://localhost:${PORT}`)})