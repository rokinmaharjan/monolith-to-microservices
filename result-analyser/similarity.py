import os
import json

original_microservices_path = "./files/refined_microservices_5.json"

with open(original_microservices_path) as f:
        original_microservices = json.load(f)


predicted_microservices_folder_path = "./ftgo_vae_5_clusters/refined/"


for json_file in os.listdir(predicted_microservices_folder_path):
    file_path = predicted_microservices_folder_path + json_file
    with open(file_path) as f:
        predicted_microservices = json.load(f)

    service_map = {}
    matched_services = set()
    total_match = 0
    for original_service in original_microservices:
        original_classes = original_microservices.get(original_service)

        max_match = 0
        corresponding_service = None
        for index, predicted_service in enumerate(predicted_microservices):
            if index in matched_services:
                continue

            predicted_classes = predicted_microservices.get(predicted_service)
            common = set(predicted_classes).intersection(original_classes)
            if (len(common) > max_match):
                max_match = len(common)
                corresponding_service = index

        service_map[original_service] = corresponding_service
        matched_services.add(corresponding_service)

        total_match = total_match + max_match

    print(json_file, ": ", total_match/68*100)

