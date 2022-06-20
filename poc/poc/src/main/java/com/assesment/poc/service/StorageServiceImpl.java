package com.assesment.poc.service;

import com.assesment.poc.model.UserComments;
import com.assesment.poc.repository.UserCommentsRepository;
import com.assesment.poc.config.StorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService {

	private final Path rootLocation;

	@Autowired
	UserCommentsRepository userCommentsRepository;

	public StorageServiceImpl(StorageConfig properties) {

		this.rootLocation = Paths.get(properties.getLocation());

	}

	@Override
	public void store(MultipartFile file, String email, String firstName, String latitude, String longitude, String comment) {

		try {

			if (file.isEmpty()) {
				throw new com.assesment.poc.Exception.StorageException("Failed to store empty file.");
			}

			String newFileName = String.valueOf(email + "-" +
													latitude + "-" +
													longitude + "-" +
													LocalDate.now().toString().replaceAll("-", "") +
													Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().indexOf(".")));

			Path destinationFile = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(newFileName)))
										.normalize().toAbsolutePath();

			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new com.assesment.poc.Exception.StorageException("Cannot store file outside current directory.");
			}

			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
				UserComments userComments = new UserComments();
				userComments.setEmail(email);
				userComments.setFirstName(firstName);
				userComments.setComment(comment);
				userComments.setLatitude(latitude);
				userComments.setLongitude(longitude);
				userComments.setPicturename(newFileName);
				userCommentsRepository.save(userComments);
			}
		}
		catch (IOException e) {
			throw new com.assesment.poc.Exception.StorageException("Failed to store file.", e);
		}

	}

	@Override
	public Stream<Path> loadAll() {

		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new com.assesment.poc.Exception.StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {

		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new com.assesment.poc.Exception.StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new com.assesment.poc.Exception.StorageFileNotFoundException("Could not read file: " + filename, e);
		}

	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {

		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new com.assesment.poc.Exception.StorageException("Could not initialize storage", e);
		}

	}
}
