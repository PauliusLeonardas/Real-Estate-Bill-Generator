package meniu;

import entities.Indicator;
import entities.Property;
import entities.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import repositories.BillRepository;
import repositories.IndicatorRepository;
import repositories.PropertyRepository;
import repositories.UtilityRepository;
import utils.DataUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static config.SystemConstants.IN;
import static config.SystemConstants.OUT;

@Slf4j
public class AccountMenuActions {

    private Scanner scanner = new Scanner(IN);

    private User user;
    private PropertyRepository propertyRepository;
    private IndicatorRepository indicatorRepository;
    private UtilityRepository utilityRepository;
    private BillRepository billRepository;

    public AccountMenuActions(PropertyRepository propertyRepository, IndicatorRepository indicatorRepository, UtilityRepository utilityRepository, BillRepository billRepository, User user) {
        this.propertyRepository = propertyRepository;
        this.indicatorRepository = indicatorRepository;
        this.utilityRepository = utilityRepository;
        this.billRepository = billRepository;
        this.user = user;
    }

    @SneakyThrows(IOException.class)
    public void accountMenuActions() throws SQLException {

        String primaryChoice = "not assigned";
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        if (!properties.isEmpty()) {
            while (!primaryChoice.equals("0")) {
                OUT.write("""

                        Urban Taxes Calculator

                        1.Check my properties
                        2.Check my indicators
                        3.Check my bills
                        4.Check my account info
                        0.Log out

                        Choice: """.getBytes());
                primaryChoice = scanner.nextLine();

                if (!primaryChoice.isBlank()) {
                    switch (primaryChoice) {
                        case "0" -> {
                            OUT.write("\n(logged out)".getBytes());
                            return;
                        }
                        // check my properties
                        case "1" -> {
                            OUT.write("Properties:\n".getBytes());
                            for (Property p : properties) {
                                OUT.write(String.format("%s (%s)\n", p.getAddress(), p.getPropertyType()).getBytes());
                            }
                        }
                        // check my indicators
                        case "2" -> userIndicators().forEach((utility, indicatorData) -> System.out.println(String.format("%s: %s", utility, indicatorData)));
                        // check my bills
                        case "3" -> {
                            BillMenuActions billMenuActions = new BillMenuActions(propertyRepository, indicatorRepository, utilityRepository, billRepository, user);
                            billMenuActions.accountBillActions();
                        }
                        // check my account info
                        case "4" -> {
                            OUT.write(String.format("""
                                    %nName: %s
                                    Lastname: %s
                                    Personal code: %s
                                    """, user.getName(), user.getLastname(), user.getPersonalCode()).getBytes());
                            OUT.write("""
                                    Properties:
                                    """.getBytes());
                            for (Property p : properties) {
                                OUT.write(String.format("* %s (%s)\n", p.getAddress(), p.getPropertyType()).getBytes());
                            }
                        }
                        default -> OUT.write("Unexpected action".getBytes());
                    }
                }
            }
        } else {
            OUT.write(String.format("\n'%s' doesn't have any properties available", user.getUsername()).getBytes());
        }
    }

    @SneakyThrows(IOException.class)
    public Map<String, String> userIndicators() throws SQLException {

        // get properties
        Set<Property> properties = propertyRepository.getPropertiesByUser(user);

        // list available type
        OUT.write("""

                Select type:
                """.getBytes());

        Map<Integer, String> types = DataUtils.elementToMap(properties.stream().map(Property::getPropertyType).distinct().collect(Collectors.toList()));

        String type = scanner.nextLine();
        String chosenType = types.get(Integer.parseInt(type));

        // prompt available addresses for type
        OUT.write("""

                Select address:
                """.getBytes());

        List<Property> chosenTypeProperties = properties.stream().filter(pr -> pr.getPropertyType().equals(chosenType)).collect(Collectors.toList());
        Map<Integer, String> addresses = DataUtils.elementToMap(chosenTypeProperties.stream().map(Property::getAddress).distinct().collect(Collectors.toList()));

        String address = scanner.nextLine();
        String chosenAddress = addresses.get(Integer.parseInt(address));

        // view indicators for selected address property
        OUT.write("""

                Indicators:
                """.getBytes());

        List<Indicator> indicators = indicatorRepository.getIndicatorsByProperty(chosenType, chosenAddress);

        //gauti tikrus indicatoriu id, o ne pagal mapo id, kad gauciau tikrus utility providerius
        Map<String, String> indicatorsMap = new HashMap<>();
        indicators.forEach(i -> {
            String utilityName = i.getUtility().getName();
            if (!utilityName.equals("Other")) {
                indicatorsMap.put(utilityName, String.format("%s - %s", i.getMonthStartAmount(), i.getMonthEndAmount()));
            }
        });

        return indicatorsMap;

    }

}