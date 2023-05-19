# This script is to discard any classes that are not common between both the monolith and microservice version

import json

# Open the file containing the JSON data
with open('./files/ftgo-monolith-dependency-graph.json') as f:
    monolith_dg = json.load(f)

# Get all the keys in the JSON data and store them in a set
monolith_classes = set()
for clazz in monolith_dg:
    # keys_set |= set(obj)
    monolith_classes.add(clazz)


with open('./files/ftgo-microservice-classes.json') as f:
    microservice_classes = json.load(f)


all_microservice_classes = set()
for key, value in microservice_classes.items():
    for clazz in value:
        all_microservice_classes.add(clazz)

# Common classes between monolith and microservice version
common_classes = monolith_classes.intersection(all_microservice_classes)
print(len(common_classes))

# Remove classes from monolith_dg not present in microservices
common_monolith_dg = {}
for key, value in monolith_dg.items():
    if key not in common_classes:
        continue

    classes = []
    for clazz in value:
        if (clazz in common_classes) and (clazz not in classes):
            classes.append(clazz)

    classes.sort()
    common_monolith_dg[key] = classes

# Final result - Microservice breakdown with only common classes
print(json.dumps(common_monolith_dg))
# print("Total classes in microservices including duplicates: ", count)
print("Common classes (Total unique classes): ", len(common_classes))
# print(common_classes)

# with open("./files/common_monolith_dependency_graph.json", "w") as f:
#     json.dump(common_monolith_dg, f)


print("===============================================================================================================")
print("===============================================================================================================")






# Probably should put this is a new file... but mehhh!!
# This script will only keep common classes in the VAE-5-Cluster results

import os
import json


# Specify the path to the folder
file_path_base = "./ftgo_vae_5_clusters/unrefined"
output_file_path_base = "./ftgo_vae_5_clusters/refined"

# Loop through all files in the folder
for json_file in os.listdir(file_path_base):
    file_path = file_path_base + "/" + json_file
    with open(file_path) as f:
        vae_result = json.load(f)


    updated_vae_result = {}
    for index, classes in enumerate(vae_result):
        updated_classes = []
        for clazz in classes:
            if (clazz in common_classes) and (clazz not in updated_classes):
                updated_classes.append(clazz)

        updated_classes.sort()
        updated_vae_result[index] = updated_classes

    print(json_file, " =========================")

    output_file = output_file_path_base + "/" + json_file
    with open(output_file, "w") as f:
        json.dump(updated_vae_result, f)

    print(json.dumps(updated_vae_result))
    