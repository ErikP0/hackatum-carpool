from cp_backend.extensions import db
import enum
import datetime

class RatingType(enum.Enum):
    DRIVER = "driver"
    PASSENGER = "passenger"

class Rating(db.Model):
    user_id = db.Column(db.Integer, primary_key=True)
    rating_type = db.Column(db.Enum(RatingType), primary_key=True)
    rating_id = db.Column(db.Integer, primary_key=True)
    rating = db.Column(db.Integer, nullable=False)
    rating_text = db.Column(db.String)
    date_submitted = db.Column(db.DateTime, nullable=False, default=datetime.datetime.utcnow)

    __tablename__ = "ratings"
    __table_args__ = (
        db.ForeignKeyConstraint(["user_id"], ["user.id"]),
    )
    
    def __init__(self, user_id, rating_type, **kwargs):
        super(Rating, self).__init__(**kwargs)

        latest_rating = Rating.query.filter_by(
            user_id=user_id,
            rating_type=rating_type
        ).last()

        if latest_rating is None:
            self.rating_id = 1
        else:
            self.rating_id = latest_rating.rating_id + 1