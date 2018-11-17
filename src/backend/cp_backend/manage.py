import click
from flask.cli import FlaskGroup

from cp_backend.app import create_app

from cp_backend.generators import load_users, load_rentals


def create_cp_backend(info):
    return create_app(cli=True)


@click.group(cls=FlaskGroup, create_app=create_cp_backend)
def cli():
    """Main entry point"""


@cli.command("init")
def init():
    """Init application, create database tables
    and create a new user named admin with password admin
    """
    from cp_backend.extensions import db
    click.echo("create database")
    db.create_all()
    click.echo("done")

    load_users(db, click)
    load_rentals(db, click)

if __name__ == "__main__":
    cli()