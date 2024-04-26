import threading
from prettytable import PrettyTable

students_data = [
    {"PRN NO.": 101, "Name": "Aarav Sharma", "Class": "FY", "Branch": "Computer Engineering"},
    {"PRN NO.": 102, "Name": "Aditi Singh", "Class": "SY", "Branch": "Information Technology"},
    {"PRN NO.": 103, "Name": "Vivek Patel", "Class": "TY", "Branch": "Electronics Engineering"},
    {"PRN NO.": 104, "Name": "Sneha Krishnan", "Class": "FY", "Branch": "Mechanical Engineering"},
    {"PRN NO.": 105, "Name": "Rohan Gupta", "Class": "SY", "Branch": "Civil Engineering"},
    {"PRN NO.": 106, "Name": "Pooja Desai", "Class": "TY", "Branch": "Electrical Engineering"},
    {"PRN NO.": 107, "Name": "Nikhil Joshi", "Class": "FY", "Branch": "Chemical Engineering"},
    {"PRN NO.": 108, "Name": "Meera Nair", "Class": "SY", "Branch": "Biotechnology"}
]

performance_data = [
    {"PRN NO.": 101, "Attendance": "90%", "Grade": "A"},
    {"PRN NO.": 102, "Attendance": "85%", "Grade": "B"},
    {"PRN NO.": 103, "Attendance": "92%", "Grade": "A"},
    {"PRN NO.": 104, "Attendance": "88%", "Grade": "B"},
    {"PRN NO.": 105, "Attendance": "95%", "Grade": "A"},
    {"PRN NO.": 106, "Attendance": "80%", "Grade": "C"},
    {"PRN NO.": 107, "Attendance": "75%", "Grade": "D"},
    {"PRN NO.": 108, "Attendance": "98%", "Grade": "A"}
]

# Function to split (partition) the data using hash function h1
def split_data(data, num_partitions):
    partitions = [[] for _ in range(num_partitions)]
    for record in data:
        prn_no = record["PRN NO."]
        partition_id = h1(prn_no, num_partitions)
        partitions[partition_id].append(record)
    return partitions

# Function to perform the join using hash function h2
def join_data(students, partitions, num_processors, partition_id):
    result = []
    for partition in partitions:
        in_memory_hash = {}
        for record in partition:
            prn_no = record["PRN NO."]
            in_memory_hash[prn_no] = record
        for student in students:
            prn_no = student["PRN NO."]
            processor_id = h2(prn_no, num_processors)
            if prn_no in in_memory_hash and processor_id == partition_id:
                record = in_memory_hash[prn_no]
                result.append({
                    "PRN NO.": prn_no,
                    "Name": student["Name"],
                    "Class": student["Class"],
                    "Branch": student["Branch"],
                    "Attendance": record["Attendance"],
                    "Grade": record["Grade"]
                })
    return result

# Hash function h1 to partition the data
def h1(prn_no, num_partitions):
    return hash(prn_no) % num_partitions

# Hash function h2 to determine where tuples should be joined
def h2(prn_no, num_processors):
    return hash(prn_no) % num_processors

# Number of partitions
num_partitions = 4

# Split the performance data using hash function h1
partitions = split_data(performance_data, num_partitions)

# Perform join on partitioned data using hash function h2
result = []
for partition_id, partition in enumerate(partitions):
    result.extend(join_data(students_data, partitions, num_partitions, partition_id))

# Display the result in a table format
result_table = PrettyTable(["PRN NO.", "Name", "Class", "Branch", "Attendance", "Grade"])
for data in result:
    result_table.add_row([
        data["PRN NO."],
        data["Name"],
        data["Class"],
        data["Branch"],
        data["Attendance"],
        data["Grade"]
    ])

print("Improved Parallel Hash Join Result:")
print(result_table)