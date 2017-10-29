This application tries to help the user remove spam.

When using the service a google apps managed email domain is needed. The script adds to-addresses to a filter based on regex rules.
Use case:

When registering an email at shadything.com the user who owns the domain example.com uses the email user.shadything.10.2017@example.com. When the system date of the script reaches month 10, year 2017, a new filter is added to gmail.com sending emails directed to <anything>.10.2017@example.com to the spam folder.


# test
gradle test

# run
gradle run

# config
src/main/resources/client_secret.json
needs to have google oath data

