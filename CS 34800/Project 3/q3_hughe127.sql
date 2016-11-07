-- I am simply moving the check to lowercase to make it easier to check.

UPDATE Name
SET Standing = 'N/A'
WHERE LOWER(Standing) NOT IN ('excellent', 'good', 'satisfactory', 'unsatisfactory', 'n/a');

ALTER TABLE Name
  ADD CHECK (LOWER(Standing) IN ('excellent', 'good', 'satisfactory', 'unsatisfactory', 'n/a'));
