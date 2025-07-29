use maBase
db.createRole({
  role: "readTransactions",
  privileges: [
    {
      resource: { db: "maBase", collection: "transactions" },
      actions: [ "find" ] // lecture seule
    }
  ],
  roles: []
})