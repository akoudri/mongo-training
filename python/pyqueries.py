import sys
import json
from pymongo import MongoClient
from bson import json_util

def export_db_as_single_file(db_name, uri="mongodb://localhost:27017/"):
    client = MongoClient(uri)
    db = client[db_name]

    data = {}
    for collection_name in db.list_collection_names():
        print(f"Extraction de {collection_name} (10 premiers documents)")
        collection = db[collection_name]
        documents = list(collection.find().limit(10))
        data[collection_name] = documents

    filename = f"{db_name}.json"
    with open(filename, "w", encoding="utf-8") as f:
        json.dump(data, f, default=json_util.default, indent=2)

    print(f"Export terminé. Données sérialisées dans {filename}.")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage : python export_db_as_single_file.py <database_name>")
        sys.exit(1)
    db_name = sys.argv[1]
    export_db_as_single_file(db_name)
