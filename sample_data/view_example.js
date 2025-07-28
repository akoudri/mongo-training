db.createView(
   "topRatedApartments",            // Le nom de notre nouvelle vue
   "listingsAndReviews",            // La collection source sur laquelle la vue est basée
   [                                // Le pipeline d'agrégation défini ci-dessus
     {
       $match: {
         "property_type": "Apartment",
         "review_scores.review_scores_cleanliness": 10,
         "accommodates": { $gt: 4 }
       }
     },
     {
       $project: {
         _id: 0,
         name: 1,
         address: 1,
         price: 1,
         accommodates: 1
       }
     },
     {
       $sort: {
         price: -1
       }
     }
   ]
)