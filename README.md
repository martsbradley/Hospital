TODO

Optimistic database checking.

Updates must be in the admin role.


#Run Integration tests.
mvn failsafe:integration-test
mvn failsafe:integration-test -Dit.test=martinbradley.hospital.persistence.repository.PatientDBRepoTestIT
