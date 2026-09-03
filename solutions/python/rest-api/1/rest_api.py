"""A small REST-like API for tracking IOUs."""

import json


class RestAPI:
    """Track users and debts between them."""

    def __init__(self, database=None):
        self.users = {
            user["name"]: user
            for user in (database or {"users": []})["users"]
        }

    def get(self, url, payload=None):
        """Handle a GET request."""
        if url == "/users":
            requested_users = self.users

            if payload is not None:
                names = json.loads(payload)["users"]
                requested_users = { name: self.users[name] for name in names }

            return json.dumps({"users": self._sorted_users(requested_users)})

        return None

    def post(self, url, payload=None):
        """Handle a POST request."""
        data = json.loads(payload)

        if url == "/add":
            user = self._new_user(data["user"])
            self.users[user["name"]] = user
            return json.dumps(user)

        if url == "/iou":
            self._lend(data["lender"], data["borrower"], data["amount"])
            updated_users = { name: self.users[name] for name in (data["lender"], data["borrower"]) }
            return json.dumps({"users": self._sorted_users(updated_users)})

        return None

    @staticmethod
    def _new_user(name):
        return {"name": name, "owes": {}, "owed_by": {}, "balance": 0.0}

    @staticmethod
    def _sorted_users(users):
        return [
            users[name]
            for name in sorted(users)
        ]

    def _lend(self, lender_name, borrower_name, amount):
        lender = self.users[lender_name]
        borrower = self.users[borrower_name]

        amount_owed_to_borrower = lender["owes"].get(borrower_name, 0.0)
        offset = min(amount, amount_owed_to_borrower)
        remaining_amount = amount - offset

        if offset:
            self._decrease_debt(lender, borrower, offset)

        if remaining_amount:
            self._increase_debt(borrower, lender, remaining_amount)

    @staticmethod
    def _decrease_debt(borrower, lender, amount):
        RestAPI._adjust_debt(borrower["owes"], lender["name"], -amount)
        RestAPI._adjust_debt(lender["owed_by"], borrower["name"], -amount)
        borrower["balance"] += amount
        lender["balance"] -= amount

    @staticmethod
    def _increase_debt(borrower, lender, amount):
        RestAPI._adjust_debt(borrower["owes"], lender["name"], amount)
        RestAPI._adjust_debt(lender["owed_by"], borrower["name"], amount)
        borrower["balance"] -= amount
        lender["balance"] += amount

    @staticmethod
    def _adjust_debt(debts, name, amount):
        debts[name] = debts.get(name, 0.0) + amount

        if debts[name] == 0:
            debts.pop(name)
