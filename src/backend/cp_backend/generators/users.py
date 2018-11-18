from cp_backend.models import User

def load_users(db, click):
    click.echo("create user")

    user = User(
        username="Christian.Schaum",
        first_name="Christian",
        last_name="Schaum",
        email="christian@example.org",
        password="christian",
        active=True
    )
    db.session.add(user)
    db.session.commit()
    click.echo("created user admin")