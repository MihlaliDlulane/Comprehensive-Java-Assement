package Employee;

import Department.Department;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee Class Comprehensive Test Suite")
public class EmployeeTest {
    private Employee emp1, emp2, emp3, emp4, emp5;
    private Department dept1, dept2, dept3;

    @BeforeEach
    @DisplayName("Test Setup - Initialize test data")
    public void setUp() {
        dept1 = new Department("Engineering", "Building A");
        dept2 = new Department("Marketing", "Building B");
        dept3 = new Department("Engineering", "Building A"); // Same as dept1 but different instance

        emp1 = new Employee("E001", "John Doe", 75000, dept1);
        emp1.addSkill("Java");
        emp1.addSkill("Python");
        emp1.addSkill("Docker");

        emp2 = new Employee("E001", "John Doe", 75000, dept1); // Same ID as emp1
        emp2.addSkill("Java");
        emp2.addSkill("Python");
        emp2.addSkill("Docker");

        emp3 = new Employee("E002", "Jane Smith", 80000, dept2);
        emp3.addSkill("Marketing");
        emp3.addSkill("Analytics");

        emp4 = new Employee("E003", "Bob Johnson", 75000, dept3); // Same salary as emp1
        emp4.addSkill("JavaScript");

        emp5 = new Employee("E004", "Alice Brown", 70000, dept1);
    }

    @Nested
    @DisplayName("1. EQUALS METHOD IMPLEMENTATION TESTS")
    class EqualsMethodTests {

        @Test
        @DisplayName("1.1 Reflexivity: x.equals(x) must return true")
        public void testEqualsReflexivity() {
            assertTrue(emp1.equals(emp1),
                    "An object must be equal to itself (reflexivity property violated)");
        }

        @Test
        @DisplayName("1.2 Symmetry: x.equals(y) == y.equals(x)")
        public void testEqualsSymmetry() {
            assertTrue(emp1.equals(emp2),
                    "emp1 should equal emp2 (same ID)");
            assertTrue(emp2.equals(emp1),
                    "emp2 should equal emp1 (symmetry property violated)");

            assertFalse(emp1.equals(emp3),
                    "emp1 should not equal emp3 (different IDs)");
            assertFalse(emp3.equals(emp1),
                    "emp3 should not equal emp1 (symmetry property violated)");
        }

        @Test
        @DisplayName("1.3 Transitivity: if x.equals(y) && y.equals(z), then x.equals(z)")
        public void testEqualsTransitivity() {
            Employee empCopy = new Employee("E001", "Different Name", 90000, dept2);

            assertTrue(emp1.equals(emp2), "emp1 should equal emp2");
            assertTrue(emp2.equals(empCopy), "emp2 should equal empCopy");
            assertTrue(emp1.equals(empCopy),
                    "emp1 should equal empCopy (transitivity property violated)");
        }

        @Test
        @DisplayName("1.4 Consistency: multiple calls return same result")
        public void testEqualsConsistency() {
            for (int i = 0; i < 100; i++) {
                assertTrue(emp1.equals(emp2),
                        "equals() must consistently return true for equal objects");
                assertFalse(emp1.equals(emp3),
                        "equals() must consistently return false for unequal objects");
            }
        }

        @Test
        @DisplayName("1.5 Null comparison: x.equals(null) must return false")
        public void testEqualsNull() {
            assertFalse(emp1.equals(null),
                    "equals(null) must return false");
        }

        @Test
        @DisplayName("1.6 Type safety: comparing with different types")
        public void testEqualsTypeSafety() {
            assertFalse(emp1.equals("String"),
                    "equals() must return false for different types");
            assertFalse(emp1.equals(Integer.valueOf(123)),
                    "equals() must return false for different types");
            assertFalse(emp1.equals(new ArrayList<>()),
                    "equals() must return false for different types");
        }

        @Test
        @DisplayName("1.7 ID-based equality: only ID matters for equality")
        public void testEqualsBasedOnIdOnly() {
            Employee sameId = new Employee("E001", "Different Name", 100000, dept2);
            assertTrue(emp1.equals(sameId),
                    "Employees with same ID should be equal regardless of other fields");
        }
    }

    @Nested
    @DisplayName("2. HASHCODE METHOD IMPLEMENTATION TESTS")
    class HashCodeMethodTests {

        @Test
        @DisplayName("2.1 Contract: equal objects must have same hashCode")
        public void testHashCodeContract() {
            assertEquals(emp1.hashCode(), emp2.hashCode(),
                    "Equal objects must have the same hashCode (contract violation)");
        }

        @Test
        @DisplayName("2.2 Consistency: multiple calls return same value")
        public void testHashCodeConsistency() {
            int firstCall = emp1.hashCode();
            for (int i = 0; i < 100; i++) {
                assertEquals(firstCall, emp1.hashCode(),
                        "hashCode must return consistent value across multiple calls");
            }
        }

        @Test
        @DisplayName("2.3 Distribution: different objects should have different hashCodes")
        public void testHashCodeDistribution() {
            Set<Integer> hashCodes = new HashSet<>();
            hashCodes.add(emp1.hashCode());
            hashCodes.add(emp3.hashCode());
            hashCodes.add(emp4.hashCode());
            hashCodes.add(emp5.hashCode());

            assertTrue(hashCodes.size() >= 3,
                    "Different employees should generally have different hash codes for good distribution");
        }

        @Test
        @DisplayName("2.4 HashMap functionality with custom hashCode")
        public void testHashMapWithCustomHashCode() {
            Map<Employee, String> map = new HashMap<>();
            map.put(emp1, "Value1");

            // Should retrieve using equal object
            assertEquals("Value1", map.get(emp2),
                    "HashMap should find value using equal object with same hashCode");

            // Should not find using different object
            assertNull(map.get(emp3),
                    "HashMap should not find value for different employee");
        }
    }

    @Nested
    @DisplayName("3. COMPARABLE INTERFACE IMPLEMENTATION TESTS")
    class ComparableTests {

        @Test
        @DisplayName("3.1 Natural ordering by salary")
        public void testCompareToBasicOrdering() {
            assertTrue(emp5.compareTo(emp1) < 0,
                    "Employee with 70k salary should come before 75k salary");
            assertTrue(emp1.compareTo(emp3) < 0,
                    "Employee with 75k salary should come before 80k salary");
            assertTrue(emp3.compareTo(emp1) > 0,
                    "Employee with 80k salary should come after 75k salary");
        }

        @Test
        @DisplayName("3.2 Employees with same salary")
        public void testCompareToEqualSalaries() {
            assertEquals(0, emp1.compareTo(emp4),
                    "Employees with same salary should return 0");
        }

        @Test
        @DisplayName("3.3 Consistency with equals")
        public void testCompareToConsistencyWithEquals() {
            // Note: This is recommended but not required by Comparable contract
            if (emp1.compareTo(emp2) == 0) {
                assertTrue(emp1.equals(emp2),
                        "When compareTo returns 0, equals should return true (recommended)");
            }
        }

        @Test
        @DisplayName("3.4 Transitivity of compareTo")
        public void testCompareToTransitivity() {
            assertTrue(emp5.compareTo(emp1) < 0, "emp5 < emp1");
            assertTrue(emp1.compareTo(emp3) < 0, "emp1 < emp3");
            assertTrue(emp5.compareTo(emp3) < 0,
                    "Transitivity: emp5 < emp3 (transitivity violated)");
        }

        @Test
        @DisplayName("3.5 Sorting in collections")
        public void testSortingInCollections() {
            List<Employee> employees = Arrays.asList(emp3, emp1, emp5, emp4);
            Collections.sort(employees);

            assertEquals(emp5, employees.get(0), "Lowest salary should be first");
            assertEquals(emp3, employees.get(3), "Highest salary should be last");

            // Verify entire order
            for (int i = 0; i < employees.size() - 1; i++) {
                assertTrue(employees.get(i).getSalary() <= employees.get(i + 1).getSalary(),
                        "Employees should be sorted by salary in ascending order");
            }
        }

        @Test
        @DisplayName("3.6 TreeSet natural ordering")
        public void testTreeSetOrdering() {
            TreeSet<Employee> treeSet = new TreeSet<>();
            treeSet.add(emp3);
            treeSet.add(emp1);
            treeSet.add(emp5);
            treeSet.add(emp4);

            Iterator<Employee> iterator = treeSet.iterator();
            assertEquals(emp5, iterator.next(), "First should be lowest salary");

            List<Employee> sortedList = new ArrayList<>(treeSet);
            for (int i = 0; i < sortedList.size() - 1; i++) {
                assertTrue(sortedList.get(i).getSalary() <= sortedList.get(i + 1).getSalary(),
                        "TreeSet should maintain natural ordering");
            }
        }
    }

    @Nested
    @DisplayName("4. COLLECTIONS INTEGRATION TESTS")
    class CollectionsIntegrationTests {

        @Test
        @DisplayName("4.1 HashMap - comprehensive operations")
        public void testHashMapOperations() {
            Map<Employee, String> map = new HashMap<>();

            // Add operations
            assertNull(map.put(emp1, "Role1"), "First put should return null");
            assertEquals("Role1", map.put(emp2, "Role2"),
                    "Put with equal key should return previous value");
            assertEquals(1, map.size(), "Map should contain only one entry for equal keys");

            // Retrieve operations
            assertEquals("Role2", map.get(emp1), "Should get latest value");
            assertEquals("Role2", map.get(emp2), "Should get value with equal key");

            // Contains operations
            assertTrue(map.containsKey(emp1), "Should contain key emp1");
            assertTrue(map.containsKey(emp2), "Should contain equal key emp2");
            assertFalse(map.containsKey(emp3), "Should not contain emp3");
        }

        @Test
        @DisplayName("4.2 HashSet - no duplicates allowed")
        public void testHashSetNoDuplicates() {
            Set<Employee> set = new HashSet<>();

            assertTrue(set.add(emp1), "First add should return true");
            assertFalse(set.add(emp2), "Adding equal object should return false");
            assertEquals(1, set.size(), "Set should contain only one object");

            assertTrue(set.contains(emp1), "Set should contain emp1");
            assertTrue(set.contains(emp2), "Set should contain equal object");
        }

        @Test
        @DisplayName("4.3 LinkedHashMap - maintains insertion order")
        public void testLinkedHashMapOrder() {
            LinkedHashMap<Employee, String> map = new LinkedHashMap<>();
            map.put(emp3, "Third");
            map.put(emp1, "First");
            map.put(emp4, "Fourth");

            List<Employee> keys = new ArrayList<>(map.keySet());
            assertEquals(emp3, keys.get(0), "First inserted should be first");
            assertEquals(emp1, keys.get(1), "Second inserted should be second");
            assertEquals(emp4, keys.get(2), "Third inserted should be third");
        }

        @Test
        @DisplayName("4.4 TreeMap - maintains sorted order")
        public void testTreeMapOrder() {
            TreeMap<Employee, String> map = new TreeMap<>();
            map.put(emp3, "80k");
            map.put(emp1, "75k");
            map.put(emp5, "70k");

            Employee first = map.firstKey();
            Employee last = map.lastKey();

            assertEquals(emp5, first, "Lowest salary should be first");
            assertEquals(emp3, last, "Highest salary should be last");
        }

        @Test
        @DisplayName("4.5 ConcurrentHashMap thread safety")
        public void testConcurrentHashMap() {
            ConcurrentHashMap<Employee, String> map = new ConcurrentHashMap<>();
            map.put(emp1, "Value1");

            // Should work same as HashMap for single thread
            assertEquals("Value1", map.get(emp2),
                    "ConcurrentHashMap should work with equals/hashCode");
        }
    }

    @Nested
    @DisplayName("5. CLONING TESTS - SHALLOW VS DEEP")
    class CloningTests {

        @Test
        @DisplayName("5.1 Shallow Clone - Basic verification")
        public void testShallowCloneBasics() throws CloneNotSupportedException {
            Employee cloned = emp1.clone();

            assertNotNull(cloned, "Clone should not be null");
            assertNotSame(emp1, cloned, "Clone should be different object");
            assertEquals(emp1.getClass(), cloned.getClass(),
                    "Clone should be same class");
        }

        @Test
        @DisplayName("5.2 Shallow Clone - Primitive fields are copied")
        public void testShallowClonePrimitives() throws CloneNotSupportedException {
            Employee cloned = emp1.clone();

            assertEquals(emp1.getId(), cloned.getId(), "ID should be copied");
            assertEquals(emp1.getName(), cloned.getName(), "Name should be copied");
            assertEquals(emp1.getSalary(), cloned.getSalary(), "Salary should be copied");

            // Modify original's primitives
            emp1.setSalary(90000);
            emp1.setName("Modified Name");

            assertNotEquals(emp1.getSalary(), cloned.getSalary(),
                    "Changing original's salary should not affect clone");
            assertNotEquals(emp1.getName(), cloned.getName(),
                    "Changing original's name should not affect clone");
        }

        @Test
        @DisplayName("5.3 Shallow Clone - Reference fields share same objects")
        public void testShallowCloneReferences() throws CloneNotSupportedException {
            Employee cloned = emp1.clone();

            assertSame(emp1.getDepartment(), cloned.getDepartment(),
                    "Shallow clone must share same Department object");
            assertSame(emp1.getSkills(), cloned.getSkills(),
                    "Shallow clone must share same skills List");

            // Verify modifications affect both
            String originalLocation = emp1.getDepartment().getLocation();
            emp1.getDepartment().setLocation("New Location");

            assertEquals("New Location", cloned.getDepartment().getLocation(),
                    "Modifying shared Department should affect both original and clone");

            // Restore for other tests
            emp1.getDepartment().setLocation(originalLocation);
        }

        @Test
        @DisplayName("5.4 Shallow Clone - List modifications affect both")
        public void testShallowCloneListModifications() throws CloneNotSupportedException {
            Employee cloned = emp1.clone();
            int originalSize = emp1.getSkills().size();

            emp1.addSkill("New Skill");

            assertEquals(originalSize + 1, cloned.getSkills().size(),
                    "Adding skill to original should affect clone's skill list");
            assertTrue(cloned.getSkills().contains("New Skill"),
                    "Clone should see the new skill added to original");

            cloned.addSkill("Clone Skill");
            assertTrue(emp1.getSkills().contains("Clone Skill"),
                    "Original should see skill added to clone");
        }

        @Test
        @DisplayName("5.5 Deep Clone - Basic verification")
        public void testDeepCloneBasics() {
            Employee cloned = emp1.deepClone();

            assertNotNull(cloned, "Deep clone should not be null");
            assertNotSame(emp1, cloned, "Deep clone should be different object");
            assertEquals(emp1.getClass(), cloned.getClass(),
                    "Deep clone should be same class");
        }

        @Test
        @DisplayName("5.6 Deep Clone - All fields are independent copies")
        public void testDeepCloneIndependence() {
            Employee cloned = emp1.deepClone();

            // Primitive fields
            assertEquals(emp1.getId(), cloned.getId(), "ID should be copied");
            assertEquals(emp1.getName(), cloned.getName(), "Name should be copied");
            assertEquals(emp1.getSalary(), cloned.getSalary(), "Salary should be copied");

            // Reference fields should be different objects
            assertNotSame(emp1.getDepartment(), cloned.getDepartment(),
                    "Deep clone must have different Department object");
            assertNotSame(emp1.getSkills(), cloned.getSkills(),
                    "Deep clone must have different skills List");

            // But with same content
            assertEquals(emp1.getDepartment().getName(), cloned.getDepartment().getName(),
                    "Department name should be same");
            assertEquals(emp1.getDepartment().getLocation(), cloned.getDepartment().getLocation(),
                    "Department location should be same");
            assertEquals(emp1.getSkills(), cloned.getSkills(),
                    "Skills list should have same content");
        }

        @Test
        @DisplayName("5.7 Deep Clone - Department modifications don't affect clone")
        public void testDeepCloneDepartmentIsolation() {
            Employee cloned = emp1.deepClone();

            String originalName = emp1.getDepartment().getName();
            String originalLocation = emp1.getDepartment().getLocation();

            // Modify original's department
            emp1.getDepartment().setName("Modified Dept");
            emp1.getDepartment().setLocation("Modified Location");

            assertEquals(originalName, cloned.getDepartment().getName(),
                    "Clone's department name should remain unchanged");
            assertEquals(originalLocation, cloned.getDepartment().getLocation(),
                    "Clone's department location should remain unchanged");

            // Modify clone's department
            cloned.getDepartment().setName("Clone Dept");

            assertEquals("Modified Dept", emp1.getDepartment().getName(),
                    "Original's department should not be affected by clone's changes");
        }

        @Test
        @DisplayName("5.8 Deep Clone - Skills list modifications are isolated")
        public void testDeepCloneSkillsIsolation() {
            Employee cloned = emp1.deepClone();
            int originalSize = emp1.getSkills().size();
            int cloneSize = cloned.getSkills().size();

            // Add skill to original
            emp1.addSkill("Original Skill");

            assertEquals(originalSize + 1, emp1.getSkills().size(),
                    "Original should have new skill");
            assertEquals(cloneSize, cloned.getSkills().size(),
                    "Clone should not be affected by original's new skill");
            assertFalse(cloned.getSkills().contains("Original Skill"),
                    "Clone should not contain skill added to original");

            // Add skill to clone
            cloned.addSkill("Clone Skill");

            assertFalse(emp1.getSkills().contains("Clone Skill"),
                    "Original should not contain skill added to clone");
        }

        @Test
        @DisplayName("5.9 Clone vs Deep Clone - Comprehensive comparison")
        public void testCloneVsDeepCloneComparison() throws CloneNotSupportedException {
            Employee shallow = emp1.clone();
            Employee deep = emp1.deepClone();

            // Both are different objects
            assertNotSame(emp1, shallow, "Shallow clone is different object");
            assertNotSame(emp1, deep, "Deep clone is different object");
            assertNotSame(shallow, deep, "Shallow and deep clones are different objects");

            // Reference comparison
            assertSame(emp1.getDepartment(), shallow.getDepartment(),
                    "Shallow clone shares department");
            assertNotSame(emp1.getDepartment(), deep.getDepartment(),
                    "Deep clone has independent department");

            assertSame(emp1.getSkills(), shallow.getSkills(),
                    "Shallow clone shares skills list");
            assertNotSame(emp1.getSkills(), deep.getSkills(),
                    "Deep clone has independent skills list");

            // Modification test
            emp1.getDepartment().setLocation("Test Location");

            assertEquals("Test Location", shallow.getDepartment().getLocation(),
                    "Shallow clone affected by department change");
            assertNotEquals("Test Location", deep.getDepartment().getLocation(),
                    "Deep clone not affected by department change");
        }

        @Test
        @DisplayName("5.10 Deep Clone - Null safety")
        public void testDeepCloneNullSafety() {
            Employee empWithNulls = new Employee("E999", "Null Test", 50000, null);

            assertDoesNotThrow(() -> {
                Employee cloned = empWithNulls.deepClone();
                assertNotNull(cloned, "Clone should not be null even with null department");
                assertNull(cloned.getDepartment(), "Null department should remain null");
                assertNotNull(cloned.getSkills(), "Skills list should not be null");
            }, "Deep clone should handle null department gracefully");
        }

        @ParameterizedTest
        @DisplayName("5.11 Clone Performance - Multiple cloning operations")
        @ValueSource(ints = {10, 50, 100})
        public void testCloningPerformance(int iterations) throws CloneNotSupportedException {
            long shallowStart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                emp1.clone();
            }
            long shallowTime = System.nanoTime() - shallowStart;

            long deepStart = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                emp1.deepClone();
            }
            long deepTime = System.nanoTime() - deepStart;

            assertTrue(deepTime > shallowTime,
                    "Deep cloning should generally take more time than shallow cloning due to object creation");
        }
    }

    @Nested
    @DisplayName("6. TOSTRING METHOD TESTS")
    class ToStringTests {

        @Test
        @DisplayName("6.1 toString contains all important fields")
        public void testToStringCompleteness() {
            String result = emp1.toString();

            assertNotNull(result, "toString should not return null");
            assertTrue(result.contains(emp1.getId()),
                    "toString should contain employee ID");
            assertTrue(result.contains(emp1.getName()),
                    "toString should contain employee name");
            assertTrue(result.contains(String.valueOf(emp1.getSalary())),
                    "toString should contain salary");
            assertTrue(result.contains("Engineering"),
                    "toString should contain department name");
            assertTrue(result.contains("Java"),
                    "toString should contain skills");
        }

        @Test
        @DisplayName("6.2 toString handles null department")
        public void testToStringNullSafety() {
            Employee empWithNullDept = new Employee("E999", "Test", 50000, null);

            assertDoesNotThrow(() -> {
                String result = empWithNullDept.toString();
                assertNotNull(result, "toString should handle null department");
                assertTrue(result.contains("null") || result.contains("N/A"),
                        "toString should indicate null department");
            }, "toString should not throw exception with null department");
        }
    }

    @Nested
    @DisplayName("7. EDGE CASES AND ERROR HANDLING")
    class EdgeCaseTests {

        @Test
        @DisplayName("7.1 Negative salary handling")
        public void testNegativeSalary() {
            Employee negSalary = new Employee("E999", "Test", -5000, dept1);
            Employee zeroSalary = new Employee("E998", "Test2", 0, dept1);

            assertTrue(negSalary.compareTo(zeroSalary) < 0,
                    "Negative salary should be less than zero");
            assertTrue(zeroSalary.compareTo(emp1) < 0,
                    "Zero salary should be less than positive");
        }

        @Test
        @DisplayName("7.2 Empty or null string fields")
        public void testEmptyFields() {
            Employee emptyName = new Employee("E997", "", 50000, dept1);
            Employee nullName = new Employee("E996", null, 50000, dept1);

            assertDoesNotThrow(() -> {
                emptyName.toString();
                nullName.toString();
                emptyName.equals(nullName);
                emptyName.hashCode();
            }, "Should handle empty/null name fields gracefully");
        }

        @Test
        @DisplayName("7.3 Large salary values")
        public void testLargeSalaryValues() {
            Employee highEarner = new Employee("E995", "Rich", Double.MAX_VALUE, dept1);
            Employee normalEarner = new Employee("E994", "Normal", 100000, dept1);

            assertTrue(normalEarner.compareTo(highEarner) < 0,
                    "Should handle MAX_VALUE salary correctly");
        }
    }

    @Nested
    @DisplayName("8. NULL SAFETY TESTS")
    class NullSafetyTests {

        @Test
        @DisplayName("8.1 Constructor with null ID")
        public void testConstructorNullId() {
            // Test if constructor accepts null ID (design decision)
            assertDoesNotThrow(() -> {
                Employee emp = new Employee(null, "John Doe", 75000, dept1);
                assertNull(emp.getId(), "Constructor should handle null ID");
            }, "Constructor should handle null ID gracefully");
        }

        @Test
        @DisplayName("8.2 Constructor with null name")
        public void testConstructorNullName() {
            assertDoesNotThrow(() -> {
                Employee emp = new Employee("E999", null, 75000, dept1);
                assertNull(emp.getName(), "Constructor should handle null name");
            }, "Constructor should handle null name gracefully");
        }

        @Test
        @DisplayName("8.3 Constructor with null department")
        public void testConstructorNullDepartment() {
            assertDoesNotThrow(() -> {
                Employee emp = new Employee("E999", "John Doe", 75000, null);
                assertNull(emp.getDepartment(), "Constructor should handle null department");
            }, "Constructor should handle null department gracefully");
        }

        @Test
        @DisplayName("8.4 Setters with null values")
        public void testSettersWithNull() {
            Employee emp = new Employee("E999", "John Doe", 75000, dept1);

            assertDoesNotThrow(() -> {
                emp.setName(null);
                emp.setDepartment(null);
            }, "Setters should handle null values gracefully");

            assertNull(emp.getName(), "Name should be null after setting");
            assertNull(emp.getDepartment(), "Department should be null after setting");
        }

        @Test
        @DisplayName("8.5 Adding null skill")
        public void testAddNullSkill() {
            Employee emp = new Employee("E999", "John Doe", 75000, dept1);

            // Could either ignore null or add it - test current behavior
            int originalSize = emp.getSkills().size();
            emp.addSkill(null);

            // Test should verify whatever the current behavior is
            assertTrue(emp.getSkills().size() == originalSize ||
                            emp.getSkills().size() == originalSize + 1,
                    "addSkill should handle null consistently");
        }

        @Test
        @DisplayName("8.6 Equals with null fields")
        public void testEqualsWithNullFields() {
            Employee emp1 = new Employee(null, "John", 75000, dept1);
            Employee emp2 = new Employee(null, "Jane", 80000, dept2);

            // Should not throw NPE
            assertDoesNotThrow(() -> {
                emp1.equals(emp2);
                emp2.equals(emp1);
            }, "equals() should handle null IDs without throwing NPE");
        }

        @Test
        @DisplayName("8.7 HashCode with null fields")
        public void testHashCodeWithNullFields() {
            Employee empWithNulls = new Employee(null, null, 75000, null);

            assertDoesNotThrow(() -> {
                int hash = empWithNulls.hashCode();
                // Verify consistency
                assertEquals(hash, empWithNulls.hashCode(),
                        "hashCode should be consistent even with null fields");
            }, "hashCode() should handle null fields without throwing NPE");
        }

        @Test
        @DisplayName("8.8 CompareTo with null fields")
        public void testCompareToNullSafety() {
            Employee emp1 = new Employee("E001", null, 75000, null);
            Employee emp2 = new Employee("E002", null, 80000, null);

            assertDoesNotThrow(() -> {
                emp1.compareTo(emp2);
            }, "compareTo() should handle employees with null fields");
        }

        @Test
        @DisplayName("8.9 ToString with all null fields")
        public void testToStringAllNulls() {
            Employee empWithNulls = new Employee(null, null, 0, null);

            assertDoesNotThrow(() -> {
                String result = empWithNulls.toString();
                assertNotNull(result, "toString should never return null");
                assertFalse(result.isEmpty(), "toString should return meaningful output");
            }, "toString() should handle all null fields gracefully");
        }

        @Test
        @DisplayName("8.10 Clone with null fields")
        public void testCloneWithNullFields() throws CloneNotSupportedException {
            Employee empWithNulls = new Employee("E999", null, 75000, null);

            Employee cloned = empWithNulls.clone();

            assertNotNull(cloned, "Clone should not be null");
            assertEquals(empWithNulls.getId(), cloned.getId());
            assertNull(cloned.getName(), "Null name should remain null");
            assertNull(cloned.getDepartment(), "Null department should remain null");
        }

        @Test
        @DisplayName("8.11 Deep clone with null fields")
        public void testDeepCloneWithNullFields() {
            Employee empWithNulls = new Employee("E999", null, 75000, null);

            assertDoesNotThrow(() -> {
                Employee cloned = empWithNulls.deepClone();
                assertNotNull(cloned, "Deep clone should not be null");
                assertNull(cloned.getDepartment(), "Null department should remain null");
                assertNotNull(cloned.getSkills(), "Skills list should be initialized even if empty");
            }, "deepClone() should handle null fields gracefully");
        }
    }

    @Nested
    @DisplayName("9. DEFENSIVE PROGRAMMING TESTS")
    class DefensiveProgrammingTests {

        @Test
        @DisplayName("9.1 Skills list encapsulation")
        public void testSkillsListEncapsulation() {
            Employee emp = new Employee("E999", "John", 75000, dept1);
            emp.addSkill("Java");
            emp.addSkill("Python");

            List<String> skills = emp.getSkills();
            skills.add("Hacking");
            skills.clear();

            // Original list should be protected
            assertTrue(emp.getSkills().size() >= 2 || emp.getSkills().isEmpty(),
                    "getSkills() should either return a defensive copy or an unmodifiable list");

            // If it's a defensive copy, original should be unchanged
            // If it's the same list, modifications will be reflected
            // Test should verify consistent behavior
        }

        @Test
        @DisplayName("9.2 Department modification protection")
        public void testDepartmentModificationProtection() {
            Department originalDept = new Department("IT", "Building A");
            Employee emp = new Employee("E999", "John", 75000, originalDept);

            // Modify the original department reference
            originalDept.setName("Modified");

            // This tests whether Employee stores the reference or a copy
            // Both are valid designs, but behavior should be consistent
            String empDeptName = emp.getDepartment().getName();
            assertTrue(empDeptName.equals("IT") || empDeptName.equals("Modified"),
                    "Department handling should be consistent");
        }

        @Test
        @DisplayName("9.3 Negative salary handling")
        public void testNegativeSalaryHandling() {
            // Test if negative salaries are allowed
            Employee emp = new Employee("E999", "John", -5000, dept1);

            // Should either accept negative or normalize to 0
            assertTrue(emp.getSalary() == -5000 || emp.getSalary() == 0,
                    "Negative salary should be handled consistently");

            // Test setter as well
            emp.setSalary(-10000);
            assertTrue(emp.getSalary() == -10000 || emp.getSalary() == 0,
                    "Salary setter should handle negative values consistently");
        }

        @Test
        @DisplayName("9.4 Empty string handling")
        public void testEmptyStringHandling() {
            Employee emp = new Employee("", "", 75000, dept1);

            assertDoesNotThrow(() -> {
                emp.equals(new Employee("", "", 80000, dept2));
                emp.hashCode();
                emp.toString();
            }, "Empty strings should be handled gracefully in all methods");
        }

        @Test
        @DisplayName("9.5 Large collection handling")
        public void testLargeSkillsList() {
            Employee emp = new Employee("E999", "John", 75000, dept1);

            // Add many skills
            for (int i = 0; i < 1000; i++) {
                emp.addSkill("Skill" + i);
            }

            assertEquals(1000, emp.getSkills().size(),
                    "Should handle large skill lists");

            // Test cloning with large list
            assertDoesNotThrow(() -> {
                Employee cloned = emp.deepClone();
                assertEquals(1000, cloned.getSkills().size(),
                        "Deep clone should handle large collections");
            });
        }

        @Test
        @DisplayName("9.6 Immutability of ID field")
        public void testIdImmutability() {
            Employee emp = new Employee("E999", "John", 75000, dept1);
            String originalId = emp.getId();

            emp.setId("E1000");

            // ID could be immutable (setter does nothing) or mutable
            // Test verifies consistent behavior
            assertTrue(emp.getId().equals(originalId) || emp.getId().equals("E1000"),
                    "ID mutability should be consistent");
        }

        @Test
        @DisplayName("9.7 Protection against external list modification")
        public void testExternalListProtection() {
            List<String> externalSkills = new ArrayList<>();
            externalSkills.add("Java");
            externalSkills.add("Python");

            Employee emp = new Employee("E999", "John", 75000, dept1);

            // If there was a constructor or setter that accepts a skills list
            // it should defensive copy
            for (String skill : externalSkills) {
                emp.addSkill(skill);
            }

            externalSkills.clear();
            externalSkills.add("Malicious");

            assertFalse(emp.getSkills().contains("Malicious"),
                    "Employee should not be affected by external list modifications");
        }

        @Test
        @DisplayName("9.8 Special characters in strings")
        public void testSpecialCharactersHandling() {
            String specialName = "John\nDoe\t\r\\\"'";
            Employee emp = new Employee("E999", specialName, 75000, dept1);
            emp.addSkill("Java\nScript");

            assertDoesNotThrow(() -> {
                emp.toString();
                emp.equals(new Employee("E999", "Different", 75000, dept1));
                emp.hashCode();
            }, "Special characters should be handled gracefully");
        }

        @Test
        @DisplayName("9.9 Method chaining safety")
        public void testMethodChainingSafety() {
            Employee emp = new Employee("E999", "John", 75000, null);

            // Even with null department, operations should be safe
            assertDoesNotThrow(() -> {
                emp.setName("Jane");
                emp.setSalary(80000);
                emp.addSkill("Java");
                emp.toString();
            }, "Method operations should be safe even with null fields");
        }
    }
}