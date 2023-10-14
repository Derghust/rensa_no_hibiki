# Rensa No Hibiki

Bi-Directional tracking for manga, book, moovies, anime...

## User

There are two way how to create a new user account.

### Anonymous Account

Anonymous account will expire after 60 days of inactivity.

Fields:

```
userID: uuid
token:  str
```

Limitations:

- 200/day APIs request
- 100 synchronized item

### Registered Account

Registered account will expire after 120 days of inactivity.
We highly reccomend to a new password or randomly generated
for better security in case of database breach (Nothing is fully 
resistante).

Fields:

```
userID:   uuid
username: str
password: str | Hashed
token:    str
```

Limitations:

- 400/day APIs request
- 200 synchronized item

### Premium Account

Premium account will contain a new field `premiumToken` which could be
used for verification.

Fields:

```
userID:       uuid
username:     str
password:     str | Hashed
token:        str
premiumToken: str
```

Limitations:

- 1600/day APIs request
- 200/month [2000 MAX] synchronized item 

## Api

### Algebras

- POST /manga/{itemName}
- POST /book/{itemName}
- POST /anime/{itemName}
- POST /moovie/{itemName}

- POST /manga/{itemID}
- POST /book/{itemID}
- POST /anime/{itemID}
- POST /moovie/{itemID}

- POST /manga/{itemName}
- POST /book/{itemName}
- POST /anime/{itemName}
- POST /moovie/{itemName}

- POST /manga/{itemName}
- POST /book/{itemName}
- POST /anime/{itemName}
- POST /moovie/{itemName}

- POST /manga/{itemID}
- POST /book/{itemID}
- POST /anime/{itemID}
- POST /moovie/{itemID}

- POST /auth/user
- POST /auth/login
- POST /auth/logout

- POST /user/{username}/{itemName}
- POST /user/{userID}/{itemName}
- POST /user/{username}/{itemID}
- POST /user/{userID}/{itemID}

### REST

### gRCP


