# Rensa No Hibiki

Bi-Directional tracking for manga, book, movies, anime...

## User

There are two way how to create a new user account.

## Api

### Algebras

All algebras endpoint will require user token or password for
authentication

```http
Content-Type: application/json
{
   "username": xyz,
   "token": abcd,
   ...
}
```

- POST /manga/{itemName}
- POST /book/{itemName}
- POST /anime/{itemName}
- POST /movie/{itemName}

- POST /manga/{itemID}
- POST /book/{itemID}
- POST /anime/{itemID}
- POST /movie/{itemID}

#### User authentication

- POST /auth/user
- POST /auth/login
- POST /auth/logout

#### Bi-Directional synchronization

```http
Content-Type: application/json
{
   "username": "1bb7f0a5-a577-4b74-b23d-9029e6e7240a",
   "password": "totallySecurePassword_:D"
   "token": "1920f4a6-ddf7-485f-9927-262dacda387d",
   "sessionToken": "0510944c-4472-47eb-a171-3dde4bf0b4bf",
   "action": "Add page"
   "value": 1
   ...
}
```

Actions:
- `Add page` - Increment synchronization page counter by value.
- `Set page` - Set synchronization page by value.
- `Set scroll` 
    - Set synchronization scroll counter by value.
    - 0 to 100



- POST /user/{username}/{itemName}
- POST /user/{userID}/{itemName}
- POST /user/{username}/{itemID}
- POST /user/{userID}/{itemID}

### REST

### gRCP

## Library Documentation

- [Pekko](https://pekko.apache.org/docs/pekko/current/typed/index.html)
- [Cats](https://typelevel.org/cats/)
- [Cats Transformer](https://typelevel.org/cats-mtl/)
- [Cats Effect](https://typelevel.org/cats-effect/docs/getting-started)
- [Monocle](https://www.optics.dev/Monocle/docs/focus)
- [Refined](https://github.com/fthomas/refined)
- [Enumeratum](https://github.com/lloydmeta/enumeratum)
- [Chimney](https://chimney.readthedocs.io/en/stable/)

**Optional library**

- [JSON - Circe](https://github.com/circe/circe)
- [Authentication - Http Session](https://github.com/softwaremill/akka-http-session)
