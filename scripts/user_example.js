use admin
db.createUser({
  user: "formationUser",
  pwd: "motDePasseSecurise",
  roles: [ { role: "readWrite", db: "maBase" } ]
})
