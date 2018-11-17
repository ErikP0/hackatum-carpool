from cp_backend.models import User

def load_users(db, click):
    click.echo("create user")

    user = User(
        username='admin',
        email='admin@mail.com',
        password='admin',
        active=True
    )
    db.session.add(user)
    db.session.commit()
    click.echo("created user admin")