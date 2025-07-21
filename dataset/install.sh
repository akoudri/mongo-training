#!/bin/bash

for db in sample*/; do
  dbname=$(basename "$db" | sed 's/^sample_//')
  for colfile in "$db"*.json; do
    colname=$(basename "$colfile" .json)
    mongoimport --db "$dbname" --collection "$colname" --file "$colfile" --drop
  done
done

