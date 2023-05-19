import os
import json

original_microservices_path = "./files/common_microservices_5.json"

with open(original_microservices_path) as f:
        original_microservices = json.load(f)


predicted_microservices_folder_path = "./ftgo_vae_5_clusters/book_no_extra_vae_5_clusters/"


for json_file in os.listdir(predicted_microservices_folder_path):
    file_path = predicted_microservices_folder_path + json_file
    with open(file_path) as f:
        predicted_microservices = json.load(f)

    service_map = {}
    matched_services = set()
    common_classes_map = {}
    total_match = 0
    for original_service in original_microservices:
        original_classes = original_microservices.get(original_service)

        max_match = 0
        corresponding_service = None
        common_classes = []
        for index, predicted_classes in enumerate(predicted_microservices):
            if index in matched_services:
                continue

            common = set(predicted_classes).intersection(original_classes)
            if (len(common) > max_match):
                max_match = len(common)
                corresponding_service = index
                common_classes = common

        service_map[original_service] = corresponding_service
        common_classes_map[original_service] = sorted(common_classes)
        matched_services.add(corresponding_service)

        total_match = total_match + max_match

    print(json_file, ": ", total_match/68*100)

    # print(service_map)
    print(common_classes_map)

